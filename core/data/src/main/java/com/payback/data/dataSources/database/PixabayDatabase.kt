package com.payback.data.dataSources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.payback.data.dataSources.database.dao.ImageDao
import com.payback.data.dataSources.database.models.ImageEntity

@Database(
    entities = [ImageEntity::class, ImageSearchResult::class],
    version = 1,
    exportSchema = false
)
abstract class PixabayDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

}