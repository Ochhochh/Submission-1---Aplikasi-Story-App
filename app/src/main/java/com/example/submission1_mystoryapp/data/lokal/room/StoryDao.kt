package com.example.submission1_mystoryapp.data.lokal.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submission1_mystoryapp.data.remote.response.ListStory

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(ListStory: List<ListStory>)

    @Query("SELECT * FROM ListStory")
    fun getAllStory(): LiveData<List<ListStory>>

    @Query("DELETE FROM ListStory")
    suspend fun deleteAll()
}