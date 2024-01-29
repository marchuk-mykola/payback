package com.payback.data.dataSources.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_search_results")
data class ImageSearchResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val query: String,
    val imageId: Int,
    val page: Int
)
