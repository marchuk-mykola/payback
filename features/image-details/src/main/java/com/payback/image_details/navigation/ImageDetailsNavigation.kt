package com.payback.image_details.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.payback.image_details.ImageDetailsRoute

@VisibleForTesting
internal const val IMAGE_ID_ARG = "imageId"

internal class ImageArgs(val imageId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
          this(checkNotNull(savedStateHandle[IMAGE_ID_ARG]) as String)
}

fun NavController.navigateToImageDetails(imageId: String) {
    navigate("image/$imageId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.imageDetailsScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "image/{$IMAGE_ID_ARG}",
        arguments = listOf(
            navArgument(IMAGE_ID_ARG) { type = NavType.StringType },
        ),
    ) {
        ImageDetailsRoute(onBackClick = onBackClick)
    }
}