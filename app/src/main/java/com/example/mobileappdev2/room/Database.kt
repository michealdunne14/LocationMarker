package com.example.mobileappdev2.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobileappdev2.models.PostModel

//@Database(entities = arrayOf(PostModel::class),version = 1,exportSchema = false)
//@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

//    abstract fun postDao(): PostDao
}