package com.dicoding.asclepius.data.api

import com.dicoding.asclepius.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getAllNews(
        @Header("Authorization") apiKey: String = "Bearer ${BuildConfig.BASE_URL}",
        @Query("q") query: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en"
    ): Response<NewsResponse>
}