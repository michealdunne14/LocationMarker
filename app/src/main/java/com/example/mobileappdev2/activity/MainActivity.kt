package com.example.mobileappdev2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileappdev2.R
import org.jetbrains.anko.AnkoLogger

class MainActivity : AppCompatActivity(),AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
