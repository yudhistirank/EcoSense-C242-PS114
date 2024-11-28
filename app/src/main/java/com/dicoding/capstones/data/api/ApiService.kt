package com.dicoding.capstones.data.api

import com.dicoding.capstones.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getAllNews(
        @Header("Authorization") apiKey: String = "Bearer ${BuildConfig.BASE_URL}",
        @Query("q") query: String = "recycle",
        @Query("category") category: String = "business",
        @Query("language") language: String = "en"
    ): Response<NewsResponse>
}