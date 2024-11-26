package com.dicoding.asclepius.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("articles")
    val articles: List<NewsItem>,

    @field:SerializedName("status")
    val status: String
) : Parcelable

@Parcelize
data class NewsItem(
    @field:SerializedName("publishedAt")
    val publishedAt: String,

    @field:SerializedName("author")
    val author: String? = "Unknown Author",

    @field:SerializedName("urlToImage")
    val urlToImage: String? = "Default Image URL",

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("source")
    val source: Source,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("content")
    val content: String
) : Parcelable

@Parcelize
data class Source(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String? = null
) : Parcelable