package com.payback.data.di

import com.payback.data.dataSources.database.dao.ImageDao
import com.payback.data.dataSources.remote.PixabayApi
import com.payback.data.repository.ImageRepository
import com.payback.domain.repositories.IImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Provides
    @Singleton
    fun provideImageRepository(
        api: PixabayApi,
        dao: ImageDao
    ): IImageRepository = ImageRepository(api, dao)

}