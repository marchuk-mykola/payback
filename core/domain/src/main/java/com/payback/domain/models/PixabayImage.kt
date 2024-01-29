package com.payback.domain.models

data class PixabayImage(
    val id: Int,
    val comments: Int,
    val downloads: Int,
    val thumbnailUrl: String,
    val largeImageURL: String?,
    val imageUrl: String?,
    val likes: Int,
    val tags: String,
    val user: String,
    val userImageUrl: String? = null,
    val userId: Int,
    val views: Int,
    val imageWidth: Int,
    val imageHeight: Int,
    val searchQuery: String? = null
) {

    val tagsSplitted: List<String>
        get() = tags.split(", ")

}
