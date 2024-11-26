package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.api.ApiConfig
import com.dicoding.asclepius.data.database.HistoryDatabase
import com.dicoding.asclepius.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database =  HistoryDatabase.getData(context)
        val dao = database.historyDao()
        return Repository.getInstance(apiService, dao)
    }
}