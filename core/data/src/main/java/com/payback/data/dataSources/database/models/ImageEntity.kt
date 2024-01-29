package com.payback.data.dataSources.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.payback.domain.models.PixabayImage

@Entity(tableName = "image_table")
data class ImageEntity(
    @PrimaryKey val id: Int,
    val comments: Int,
    val downloads: Int,
    val largeImageURL: String?,
    val imageUrl: String?,
    val likes: Int,
    val tags: String,
    val user: String,
    val userId: Int,
    val views: Int,
    val imageHeight: Int,
    val imageWidth: Int,
    val thumbnailUrl: String,
    val userImageUrl: String? = null,
    val searchQuery: String? = null
)


fun ImageEntity.toDomain() = PixabayImage(
    id = id,
    comments = comments,
    downloads = downloads,
    largeImageURL = largeImageURL,
    likes = likes,
    tags = tags,
    user = user,
    userId = userId,
    views = views,
    searchQuery = searchQuery,
    imageUrl = imageUrl,
    imageHeight = imageHeight,
    imageWidth = imageWidth,
    thumbnailUrl = thumbnailUrl,
    userImageUrl = userImageUrl
)
