package com.example.submission1_mystoryapp.di

import android.content.Context
import com.example.submission1_mystoryapp.data.StoryRepository
import com.example.submission1_mystoryapp.data.lokal.room.StoryDatabase
import com.example.submission1_mystoryapp.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        val dao = database.storyDao()
        return StoryRepository.getInstance(apiService, dao)
    }
}