package com.example.mobileappdev2

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    val landmarks = MemoryStore()

    override fun onCreate() {
        super.onCreate()
        info { "App has Started" }
    }
}