package com.example.mobileappdev2.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobileappdev2.PostModel

@Database(entities = arrayOf(PostModel::class),version = 1,exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun postDao(): PostDao
}