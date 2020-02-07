package com.example.mobileappdev2.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    val imagesType = object : TypeToken<ArrayList<String>>(){}.type

    // For converting the listing of images
    @TypeConverter
    fun stringToList(value: String?): List<String>? {
        if (value == null) {
            return ArrayList()
        }

        return Gson().fromJson(value, imagesType)
    }
}