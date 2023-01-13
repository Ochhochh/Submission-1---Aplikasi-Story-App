package com.example.submission1_mystoryapp.view.activity.main

import androidx.lifecycle.ViewModel
import com.example.submission1_mystoryapp.data.StoryRepository

class MainViewModel (private val storyRepository: StoryRepository): ViewModel() {
    fun getStory(token: String) = storyRepository.getAllStory(token)
}