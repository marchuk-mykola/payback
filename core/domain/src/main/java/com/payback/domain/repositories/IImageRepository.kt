package com.payback.domain.repositories

import com.payback.domain.models.Page
import com.payback.domain.models.PixabayImage

interface IImageRepository {

    suspend fun loadPage(
        query: String,
        page: Int
    ): Result<Page>

    suspend fun getImageDetails(
        imageId: Int
    ): PixabayImage?

}

