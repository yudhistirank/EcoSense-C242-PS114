package com.example.ecosense.network

import com.example.ecosense.models.Article
import com.example.ecosense.models.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/articles")
    fun getArticles(): Call<List<Article>>

    @Multipart
    @POST("/predict")
    fun predictWaste(
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>
}
