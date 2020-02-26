package com.example.mobileappdev2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.TabsPagerAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult

class MainActivity : AppCompatActivity(),AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
