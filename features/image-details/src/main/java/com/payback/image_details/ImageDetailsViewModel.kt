package com.payback.image_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.payback.domain.models.PixabayImage
import com.payback.domain.repositories.IImageRepository
import com.payback.image_details.navigation.ImageArgs
import com.presentation.mvi.BaseViewModel
import com.presentation.mvi.Mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ImageDetailsState(
    val image: PixabayImage? = null
) : Mvi.ViewState {

    companion object {
        val initial = ImageDetailsState()
    }

}


sealed class ImageDetailsAction : Mvi.ViewAction {
    data class LoadImageDetails(val imageId: Int) : ImageDetailsAction()
}


@HiltViewModel
class ImageDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: IImageRepository,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<ImageDetailsState, ImageDetailsAction, Mvi.Stub>(
    ImageDetailsState.initial
) {

    private val imageArgs = ImageArgs(savedStateHandle)

    init {
        handleLoadImageDetails(imageArgs.imageId.toInt())
    }

    override fun process(action: ImageDetailsAction) {
        when (action) {
            is ImageDetailsAction.LoadImageDetails -> handleLoadImageDetails(action.imageId)
        }
    }

    private fun handleLoadImageDetails(imageId: Int) {
        viewModelScope.launch(dispatcher) {
            val result = repository.getImageDetails(imageId)
            emit { copy(image = result) }
        }
    }


}