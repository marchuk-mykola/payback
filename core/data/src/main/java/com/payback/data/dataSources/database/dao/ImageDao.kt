package com.payback.data.dataSources.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.payback.data.dataSources.database.ImageSearchResult
import com.payback.data.dataSources.database.models.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM image_table WHERE id IN (SELECT imageId FROM image_search_results WHERE `query` = :query AND page = :page)")
    suspend fun getImages(query: String, page: Int): List<ImageEntity>

    @Query("SELECT COUNT(id) > 0 FROM image_search_results WHERE `query` = :query AND page = :nextPage")
    suspend fun hasNextPage(query: String, nextPage: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImages(images: List<ImageEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSearchResult(result: ImageSearchResult)

    @Query("SELECT COUNT(id) > 0 FROM image_search_results WHERE `query` = :query AND page = :page")
    suspend fun hasDataForQueryAndPage(query: String, page: Int): Boolean

    @Query("SELECT * FROM image_table WHERE id = :id")
    suspend fun getImageById(id: Int): ImageEntity?

}