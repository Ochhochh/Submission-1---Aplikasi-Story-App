package com.example.submission1_mystoryapp.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.submission1_mystoryapp.data.lokal.room.StoryDao
import com.example.submission1_mystoryapp.data.remote.ApiService
import com.example.submission1_mystoryapp.data.remote.response.ListStory
import com.example.submission1_mystoryapp.data.remote.response.LoginResult
import com.example.submission1_mystoryapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.Exception

class StoryRepository (
    private val apiService: ApiService,
    private val storyDao: StoryDao,
) {

    fun login(email: String, password: String): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response.loginResult))
        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
            Log.d(ContentValues.TAG, "onFailure: ${e.message.toString()}")
        }
    }

    fun signup (name: String, email: String, password: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        val registerResponse = MutableLiveData<StoryResponse>()
        try {
            val response = apiService.signup(name, email, password)
            registerResponse.postValue(response)
            emit(Result.Success(response.message))
        } catch (e: Exception){
            Log.d(ContentValues.TAG, "onFailure: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addNewStory(token: String, image: MultipartBody.Part, description: RequestBody): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addNewStory(token, image, description)
            emit(Result.Success(response.message))
        } catch (e: Exception){
            Log.d(ContentValues.TAG, "onFailure: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllStory(token: String) : LiveData<Result<List<ListStory>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories(token)
            val listStory = response.listStory
            val newStory = listStory.map { story ->
                ListStory(
                    story.id,
                    story.name,
                    story.description,
                    story.photoUrl
                )
            }
            storyDao.deleteAll()
            storyDao.insertStory(newStory)
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "onFailure: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<ListStory>>> = storyDao.getAllStory().map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            storyDao: StoryDao
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDao)
            }.also { instance = it }
    }
}