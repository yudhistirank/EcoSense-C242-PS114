package com.example.ecosense.models

data class DetailArticleResponse(
    val status: String,             // Status respons (e.g., "success")
    val data: Article               // Detail artikel
)