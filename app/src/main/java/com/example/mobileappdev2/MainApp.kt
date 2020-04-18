package com.example.mobileappdev2

import android.app.Application
import com.example.mobileappdev2.firebase.FireStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

//  JSON Store for database
//    lateinit var landmarks: MemoryStore
//  Room Database
//    lateinit var landmarks: MemoryStoreRoom
    lateinit var fireStore: FireStore

    override fun onCreate() {
        super.onCreate()
        info { "App has Started" }
//        landmarks = MemoryStoreRoom(applicationContext)
//        landmarks = MemoryStore(applicationContext)
        fireStore = FireStore(applicationContext)
    }
}