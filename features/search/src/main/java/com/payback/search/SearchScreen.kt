@file:OptIn(ExperimentalMaterial3Api::class)

package com.payback.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.payback.domain.models.PixabayImage
import com.presentation.designSystem.components.SearchTextField

@Composable
fun SearchScreen(
    navigateToDetails: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    SearchUi(state = state, processAction = viewModel::process, navigateToDetails = navigateToDetails)
}

@Composable
fun SearchUi(
    state: SearchState,
    processAction: (SearchAction) -> Unit,
    navigateToDetails: (Int) -> Unit
) {
    val lazyListState = rememberLazyStaggeredGridState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            SearchField(
                modifier = Modifier.statusBarsPadding(),
                query = state.query,
                onQueryChanged = {
                    processAction(SearchAction.Search(it))
                }
            )
        }
    ) { it ->
        LazyVerticalStaggeredGrid(
            state = lazyListState,
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(
                state.listItems,
                key = null,
                span = { index, item ->
                    getItemSpan(item = item)
                }
            ) { index, item ->
                ListItemContent(
                    item = item,
                    navigateToDetails = {
                        processAction(SearchAction.SelectImage(it))
                    },
                    refetchAction = { processAction(SearchAction.Refetch)}
                )

                EndOfListReached {
                    processAction(SearchAction.LoadNextPage)
                }
            }
        }
    }

    if (state.selectedImageId != null) {
        ConfirmationDialog(
            onDismissRequest = {
                processAction(SearchAction.SelectImage(null))
            },
            onConfirmation = {
                processAction(SearchAction.SelectImage(null))
                navigateToDetails(state.selectedImageId)
            }
        )
    }
}


@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Example Icon")
        },
        title = {
            Text(text = stringResource(R.string.do_you_want_to_open_the_image))
        },
        text = {
            Text(text = stringResource(R.string.you_are_about_to_open_the_image_in_a_new_screen))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@Composable
fun ListItemContent(
    item: ListItem,
    navigateToDetails: (Int) -> Unit,
    refetchAction: () -> Unit,
) {
    when (item) {
        is ListItem.Image -> ImageItem(image = item.image, onImageClick = navigateToDetails)
        is ListItem.Error -> ErrorItem { refetchAction() }
        is ListItem.Loading -> LoadingItem()
        ListItem.NoInternetConnection -> NoInternetConnectionItem()
        ListItem.EmptyResult -> EmptyResultItem()
    }
}


@Composable
fun EmptyResultItem(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        text = stringResource(R.string.no_results_found),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    SearchTextField(
        modifier = modifier
            .fillMaxWidth(),
        query = query,
        onQueryChanged = onQueryChanged,
    )
}

@Composable
fun EndOfListReached(onEndReached: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        onEndReached()
    }
}

@Composable
fun NoInternetConnectionItem(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        text = stringResource(R.string.no_internet_connection),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoadingItem(
    modifier: Modifier = Modifier
) {
    // I used Text instead CircularProgressBar because of a bug in the newest version of compose
    // https://github.com/JetBrains/compose-multiplatform/issues/4157
    Text(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        text = stringResource(R.string.loading),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    onRefreshClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = stringResource(R.string.error_occurred),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRefreshClick
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.retry),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    image: PixabayImage,
    onImageClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp))
            .clickable {
                onImageClick(image.id)
            },
    ) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(image.imageWidth.toFloat() / image.imageHeight.toFloat())
                .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)),
            model = image.thumbnailUrl,
            contentDescription = "image ${image.id} of ${image.user} for search query ${image.searchQuery}",
            filterQuality = FilterQuality.Low,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.error_image_generic),
            fallback = painterResource(R.drawable.error_image_generic),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = image.user,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Start
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = image.tags,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

}


fun getItemSpan(item: ListItem): StaggeredGridItemSpan {
    return when (item) {
        is ListItem.Image, is ListItem.Loading -> StaggeredGridItemSpan.SingleLane
        else -> StaggeredGridItemSpan.FullLine
    }
}