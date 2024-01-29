package com.payback.data.dataSources.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    companion object {
        private const val API_KEY_QUERY_PARAM = "key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.url.queryParameter(API_KEY_QUERY_PARAM) != null) {
            return chain.proceed(originalRequest)
        }

        val newUrl = originalRequest.url
            .newBuilder()
            .addQueryParameter(API_KEY_QUERY_PARAM, "42064911-09347c038a8b3bd8dbbc7cac0") // move to build config
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

}