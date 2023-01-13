package com.example.submission1_mystoryapp.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GetStoryResponse (

    @field:SerializedName("error")
    val error: Boolean,

) : Parcelable

@Parcelize
@Entity(tableName = "ListStory")
data class ListStory(

    @field:PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,
) :Parcelable