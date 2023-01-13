package com.example.submission1_mystoryapp.view.activity.login

import androidx.lifecycle.ViewModel
import com.example.submission1_mystoryapp.data.StoryRepository

class LoginViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun login(email: String, password: String) = storyRepository.login(email, password)
}