package com.example.ecosense.models


data class HistoryResponse(
    val data: List<HistoryItem>, // A list of HistoryItem objects
    val status: String
)

data class HistoryItem(
    val history: HistoryDetail, // A nested object containing the history details
)

data class HistoryDetail(
    val createdAt: String,  // Date when the history was created
    val id: String,         // Unique ID for the history
    val result: String,     // Result of the prediction (e.g., Organik, Anorganik)
    val suggestion: String  // Suggestion related to the result (e.g., recycling suggestion)
)

