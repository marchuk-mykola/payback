package com.payback.data.dataSources.database.di

import android.content.Context
import androidx.room.Room
import com.payback.data.dataSources.database.PixabayDatabase
import com.payback.data.dataSources.database.dao.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providePixabayDatabase(@ApplicationContext appContext: Context): PixabayDatabase {
        return Room.databaseBuilder(
            appContext,
            PixabayDatabase::class.java,
            "PIXABAY_DATABASE"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideImageDao(database: PixabayDatabase): ImageDao {
        return database.imageDao()
    }

}