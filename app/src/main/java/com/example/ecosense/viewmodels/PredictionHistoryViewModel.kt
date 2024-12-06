package com.example.ecosense.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecosense.models.HistoryResponse

class PredictionHistoryViewModel : ViewModel() {

    private val _historyResponse = MutableLiveData<List<HistoryResponse>>()
    val historyResponse: LiveData<List<HistoryResponse>> get() = _historyResponse

    // Initialize with an empty list
    init {
        _historyResponse.value = emptyList()
    }

    // Method to add history response to the list
    fun addHistoryResponse(history: HistoryResponse) {
        val currentList = _historyResponse.value ?: emptyList()
        _historyResponse.value = currentList + history
    }
}
