package com.dicoding.capstones.di

import android.content.Context
import com.dicoding.capstones.data.api.ApiConfig
import com.dicoding.capstones.data.database.HistoryDatabase
import com.dicoding.capstones.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database =  HistoryDatabase.getData(context)
        val dao = database.historyDao()
        return Repository.getInstance(apiService, dao)
    }
}