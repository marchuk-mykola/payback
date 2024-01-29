package com.payback.search

import com.payback.domain.models.PixabayImage
import com.presentation.mvi.Mvi

data class SearchState(
    val pages: List<PageState>,
    val selectedImageId: Int?,
    val query: String,
    val pageToFetch: Int = 1,
    val isEndOfList: Boolean = false,
) : Mvi.ViewState {

    val listItems: List<ListItem>
        get() = pages.toListItems()

    val isLoading get() = pages.contains(PageState.Loading)

    val isError get() = pages.firstOrNull { it is PageState.Error } != null

    val canLoadMore get() = !isLoading && !isEndOfList

}


sealed class PageState {
    data object Loading : PageState()
    data class Success(val data: List<PixabayImage>) : PageState()
    data object EmptyResult : PageState()
    data class Error(val error: Throwable) : PageState()
    data object NoInternetConnection : PageState()

    val listItems
        get() = when (this) {
            is Loading -> listOf(ListItem.Loading)
            is Success -> data.map { ListItem.Image(it) }
            is Error -> listOf(ListItem.Error)
            NoInternetConnection -> listOf(ListItem.NoInternetConnection)
            is EmptyResult -> listOf(ListItem.EmptyResult)
        }

}

fun List<PageState>.toListItems(): List<ListItem> {
    return flatMap { it.listItems }
}

sealed class ListItem {
    data class Image(val image: PixabayImage) : ListItem()
    data object Loading : ListItem()
    data object Error : ListItem()
    data object NoInternetConnection : ListItem()
    data object EmptyResult : ListItem()
}

sealed class SearchAction : Mvi.ViewAction {
    data class Search(val query: String) : SearchAction()

    data class SelectImage(val imageId: Int?) : SearchAction()

    data object Refetch : SearchAction()
    data object LoadNextPage : SearchAction()
}
