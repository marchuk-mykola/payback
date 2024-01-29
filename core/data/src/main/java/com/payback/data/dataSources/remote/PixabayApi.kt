package com.payback.data.dataSources.remote

import com.payback.data.dataSources.remote.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    @GET(".")
    suspend fun searchImages(
        @Query("q") query: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null,
    ): SearchResponse

}