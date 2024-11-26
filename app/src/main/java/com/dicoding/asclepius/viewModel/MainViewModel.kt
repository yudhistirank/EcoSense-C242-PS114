package com.dicoding.asclepius.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.api.NewsItem
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.data.repository.Repository
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainViewModel(private val cancerRepository: Repository) : ViewModel() {
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _cancerNews = MutableLiveData<List<NewsItem>?>()
    val cancerNews: LiveData<List<NewsItem>?> = _cancerNews

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    private val _history = MutableLiveData<List<History?>>()
    val history: LiveData<List<History?>> = _history

    private val _historyById = MutableLiveData<History>()
    val historyById: LiveData<History> = _historyById

    init {
        listCancerNews()
        listHistory()
    }

    fun listCancerNews() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = cancerRepository.getNews()
            result.onSuccess {
                _cancerNews.value = it
                _isLoading.value = false
                clearErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
                _isLoading.value = false
            }
        }
    }

    fun listHistory() {
        _isLoading.value = true
        cancerRepository.getAllHistory().observeForever { history ->
            Log.d("MainViewModel", "History: $history")
            _isLoading.value = false
            _history.value = history
            clearErrorMessage()
        }
    }

    fun historyById(id: String?) {
        _isLoading.value = true
        cancerRepository.getHistoryById(id).observeForever { history ->
            _isLoading.value = false
            _historyById.value = history
            clearErrorMessage()
        }
    }

    fun insertHistory(uri: Uri, classifications: List<Classifications>) {
        viewModelScope.launch {
            val uriString = uri.toString()
            val resultString =
                classifications.joinToString { it.categories.joinToString { category -> category.label } }
            val percentageString = classifications.joinToString {
                it.categories.joinToString { category ->
                    NumberFormat.getPercentInstance()
                        .format(category.score).trim()
                }
            }
            val success = cancerRepository.insertHistory(uriString, resultString, percentageString)
            if (!success) {
                _errorMessage.value = "Failed to save data"
            }
        }
    }

    fun saveUri(uri: Uri) {
        viewModelScope.launch {
            _currentImageUri.value = uri
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}