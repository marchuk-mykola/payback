package com.payback.data.dataSources.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PixabayImageDto(
    @Json(name = "comments")
    val comments: Int,
    @Json(name = "downloads")
    val downloads: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "imageHeight")
    val imageHeight: Int,
    @Json(name = "imageSize")
    val imageSize: Int,
    @Json(name = "imageURL")
    val imageURL: String?,
    @Json(name = "imageWidth")
    val imageWidth: Int,
    @Json(name = "largeImageURL")
    val largeImageURL: String?,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "pageURL")
    val pageURL: String?,
    @Json(name = "tags")
    val tags: String,
    @Json(name = "user")
    val user: String,
    @Json(name = "user_id")
    val userId: Int,
    @Json(name = "userImageURL")
    val userImageURL: String,
    @Json(name = "previewURL")
    val previewUrl: String,
    @Json(name = "views")
    val views: Int,
)


fun PixabayImageDto.toEntity(query: String) =
    com.payback.data.dataSources.database.models.ImageEntity(
        id = id,
        comments = comments,
        downloads = downloads,
        largeImageURL = largeImageURL,
        likes = likes,
        tags = tags,
        user = user,
        userId = userId,
        views = views,
        searchQuery = query,
        imageUrl = imageURL,
        imageHeight = imageHeight,
        imageWidth = imageWidth,
        thumbnailUrl = previewUrl,
        userImageUrl = userImageURL
    )