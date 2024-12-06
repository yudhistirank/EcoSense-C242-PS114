package com.example.ecosense.models


data class ArticleResponse(
    val status: String,
    val data: Data
)

data class Data(
    val articles: List<Article>
)

data class Article(
    val id: String,
    val title: String,
    val category: String,
    val description: String
)

