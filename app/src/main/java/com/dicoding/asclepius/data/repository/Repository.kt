package com.dicoding.asclepius.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.api.ApiService
import com.dicoding.asclepius.data.api.NewsItem
import com.dicoding.asclepius.data.database.HistoryDao
import com.dicoding.asclepius.data.model.History

class Repository(
    private val historyDao: HistoryDao,
    private val apiService: ApiService
) {
    suspend fun getNews(): Result<List<NewsItem>> {
        return try {
            val response = apiService.getAllNews()
            if (response.isSuccessful) {
                Result.success(response.body()?.articles ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load data from Api, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllHistory(): LiveData<List<History>> {
        return historyDao.getAllHistory()
    }

    fun getHistoryById(id: String?): LiveData<History> {
        return historyDao.getHistoryById(id)
    }

    suspend fun insertHistory(uri: String, category: String, percentage: String): Boolean {
        return try {
            val historyDB = History(
                uri = uri,
                category = category,
                percentage = percentage
            )
            val insertResult = historyDao.insertHistory(historyDB)
            Log.d("Insert History", "History: $insertResult")
            insertResult != -1L
        } catch (e: Exception) {
            Log.e("Insert History", "Error inserting history: ${e.message}")
            false
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            historyDao: HistoryDao
        ):Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(historyDao, apiService)
            }
                .also { instance = it }
    }
}