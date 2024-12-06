package com.example.ecosense.network

import com.example.ecosense.models.ArticleResponse
import com.example.ecosense.models.DetailArticleResponse
import com.example.ecosense.models.HistoryResponse
import com.example.ecosense.models.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("/articles")
    fun getArticles(): Call<ArticleResponse>

    @GET("/articles/{id}")
    fun getArticleDetail(
        @Path("id") id: String
    ): Call<DetailArticleResponse>

    @GET("/articles")
    fun getArticlesByCategory(
        @Query("category") category: String
    ): Call<ArticleResponse>

    @Multipart
    @POST("/predict")
    fun predictWaste(
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>

    @GET("/predict/histories")
    suspend fun getPredictionHistories(): Response<HistoryResponse>

}
