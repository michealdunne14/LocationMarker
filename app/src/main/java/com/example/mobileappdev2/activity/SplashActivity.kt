package com.example.mobileappdev2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.login.LoginActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class SplashActivity : AppCompatActivity(),AnkoLogger {
    lateinit var app : MainApp

    override fun onStart() {
        super.onStart()
        app.fireStore.clearPosts()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        app = application as MainApp

        info { "Splash Screen Started" }

        if (app.fireStore.currentUserId().isEmpty()){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right)
            startActivity(Intent(baseContext, LoginActivity::class.java))
            finish()
        }else{
            app.fireStore.fetchFavourites{
            }
            app.fireStore.fetchPosts {
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right)
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }
        }
    }
}
