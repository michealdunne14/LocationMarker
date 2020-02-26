package com.example.mobileappdev2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileappdev2.R
import com.example.mobileappdev2.login.LoginActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class SplashActivity : AppCompatActivity(),AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        info { "Splash Screen Started" }
        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                    finish()
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
