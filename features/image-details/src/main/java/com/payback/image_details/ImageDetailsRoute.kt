@file:OptIn(ExperimentalMaterial3Api::class)

package com.payback.image_details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.payback.domain.models.PixabayImage

@Composable
internal fun ImageDetailsRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: ImageDetailsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val image = uiState.image ?: return

    ImageDetailsScreen(
        modifier = modifier,
        image = image,
        onBackClick = onBackClick
    )
}

@Composable
internal fun ImageDetailsScreen(
    modifier: Modifier = Modifier,
    image: PixabayImage,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ImageDetailsTopBar(
                image = image,
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            PixabayImageUi(
                modifier = Modifier
                    .fillMaxSize(),
                image = image
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Tags(
                    tags = image.tagsSplitted
                )
                UserUi(
                    image = image
                )
                IconsRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    image = image
                )

            }
        }
    }
}


@Composable
fun Tags(
    modifier: Modifier = Modifier,
    tags: List<String>
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            Text(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                text = tag,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun UserUi(
    modifier: Modifier = Modifier,
    image: PixabayImage
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            model = image.userImageUrl,
            contentDescription = "user image",
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.High,
        )
        Text(
            modifier = Modifier.wrapContentSize(),
            text = image.user,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun IconsRow(
    modifier: Modifier = Modifier,
    image: PixabayImage
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconWithText(
            contentDescription = "likes",
            drawable = R.drawable.ic_like,
            text = image.likes.toString()
        )

        IconWithText(
            contentDescription = "downloads",
            drawable = R.drawable.ic_downloads,
            text = image.downloads.toString()
        )

        IconWithText(
            contentDescription = "comments",
            drawable = R.drawable.ic_comments,
            text = image.comments.toString()
        )
    }
}


@Composable
fun IconWithText(
    modifier: Modifier = Modifier,
    contentDescription: String,
    @DrawableRes drawable: Int,
    text: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = drawable),
            contentDescription = contentDescription
        )
        Text(text = text)
    }
}

@Composable
fun ImageDetailsTopBar(
    image: PixabayImage,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = image.user)
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "back")
            }
        }
    )
}

@Composable
fun PixabayImageUi(
    modifier: Modifier = Modifier,
    image: PixabayImage
) {
    AsyncImage(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(image.imageWidth.toFloat() / image.imageHeight.toFloat()),
        model = image.largeImageURL,
        contentDescription = "image details",
    )
}