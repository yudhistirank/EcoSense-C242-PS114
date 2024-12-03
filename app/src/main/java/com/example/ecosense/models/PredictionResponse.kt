package com.example.ecosense.models

data class PredictionResponse(
    val message: String,
    val status: String,
    val data: PredictionData
)

data class PredictionData(
    val createdAt: String,
    val id: String,
    val result: String,
    val suggestion: String
)

