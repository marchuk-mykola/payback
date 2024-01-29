package com.payback.data.repository

import com.payback.data.dataSources.database.ImageSearchResult
import com.payback.data.dataSources.database.dao.ImageDao
import com.payback.data.dataSources.database.models.toDomain
import com.payback.data.dataSources.remote.PixabayApi
import com.payback.data.dataSources.remote.models.SearchResponse
import com.payback.data.dataSources.remote.models.toEntity
import com.payback.domain.models.Page
import com.payback.domain.models.PixabayImage
import com.payback.domain.repositories.IImageRepository
import retrofit2.HttpException
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val api: PixabayApi,
    private val dao: ImageDao
) : IImageRepository {

    override suspend fun loadPage(query: String, page: Int): Result<Page> {
        return try {
            if (!dao.hasDataForQueryAndPage(query, page)) {
                val response = fetchAndCacheImages(query, page)

                return Result.success(
                    Page(
                        response.images.map { it.toEntity(query).toDomain() },
                        response.totalHits <= page * 20 || response.images.size < 20
                    )
                )
            }

            val images = dao.getImages(query, page).map { it.toDomain() }
            val isLastPage = !dao.hasNextPage(query, page + 1)

            Result.success(Page(images, isLastPage))
        } catch (e: Exception) {
            if (e is HttpException && e.code() == 400) {
                return Result.success(Page(emptyList(), true))
            }

            Result.failure(e)
        }
    }

    private suspend fun fetchAndCacheImages(query: String, page: Int): SearchResponse {
        val response = api.searchImages(query, 20, page)
        val imageEntities = response.images.map { it.toEntity(query) }
        dao.insertAllImages(imageEntities)
        imageEntities.forEach { image ->
            dao.insertSearchResult(ImageSearchResult(query = query, imageId = image.id, page = page))
        }

        return response
    }

    override suspend fun getImageDetails(imageId: Int): PixabayImage? {
        return dao.getImageById(imageId)?.toDomain()
    }
}
