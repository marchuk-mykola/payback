package com.payback.search

import androidx.lifecycle.viewModelScope
import com.payback.domain.models.Page
import com.payback.domain.repositories.IImageRepository
import com.presentation.mvi.BaseViewModel
import com.presentation.mvi.Mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: IImageRepository
) : BaseViewModel<SearchState, SearchAction, Mvi.Stub>(
    initialState = SearchState(
        pages = emptyList(),
        selectedImageId = null,
        query = ""
    )
) {

    init {
        searchImages("fruits")
    }

    private var job: Job? = null

    override fun process(action: SearchAction) {
        when (action) {
            is SearchAction.Search -> handleSearchAction(action)
            is SearchAction.SelectImage -> updateSelectedImage(action.imageId)
            SearchAction.LoadNextPage -> loadNextPage()
            SearchAction.Refetch -> refetchImages()
        }
    }

    private fun loadNextPage() = searchImages(currentState.query)

    private fun refetchImages() = searchImages(currentState.query)

    private fun handleSearchAction(action: SearchAction.Search) {
        searchImages(action.query)
    }

    private fun updateSelectedImage(imageId: Int?) {
        emit { copy(selectedImageId = imageId) }
    }

    private fun searchImages(query: String) {
        if (shouldNotProceedWithQuery(query)) return
        cancelPreviousJob()
        setLoadingState()

        job = viewModelScope.launch(Dispatchers.IO) {
            repository.loadPage(query, currentState.pageToFetch).handleResult()
        }.also { it.start() }
    }

    private fun shouldNotProceedWithQuery(query: String): Boolean {
        return if (currentState.query != query) resetStateForNewSearch(query)
        else !currentState.canLoadMore
    }

    private fun resetStateForNewSearch(query: String): Boolean {
        emit { SearchState(pages = emptyList(), selectedImageId = null, query = query) }
        return false
    }

    private fun cancelPreviousJob() {
        job?.cancel()
    }

    private fun setLoadingState() {
        emit { copy(pages = pages.filterIsInstance<PageState.Success>() + PageState.Loading) }
    }

    private suspend fun Result<Page>.handleResult() {
        onSuccess { result -> updateStateWithSuccess(result) }
        onFailure { throwable -> handleFailure(throwable) }
    }

    private fun updateStateWithSuccess(result: Page) {
        val isEmptyResult = result.list.isEmpty() && currentState.pageToFetch == 1
        val newPages = if (isEmptyResult) PageState.EmptyResult else PageState.Success(result.list)

        emit {
            copy(
                pages = pages.filterIsInstance<PageState.Success>() + newPages,
                pageToFetch = pageToFetch + 1,
                isEndOfList = result.isLastPage
            )
        }
    }

    private fun handleFailure(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException -> emit { copy(pages = pages.filterIsInstance<PageState.Success>() + PageState.NoInternetConnection) }
            !is IOException -> emit {
                copy(
                    pages = pages.filterIsInstance<PageState.Success>() + PageState.Error(
                        throwable
                    )
                )
            }
        }
    }


}