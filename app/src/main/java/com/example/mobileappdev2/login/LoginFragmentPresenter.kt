package com.example.mobileappdev2.login

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.activity.MainActivity
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.base.VIEW
import com.example.mobileappdev2.firebase.FireStore
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class LoginFragmentPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }

    fun doLogin(email: String, password:String){
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    app.fireStore.fetchPosts {
                        view.gotoMainPage()
                    }
                } else {
//                  If user does not exist then send appropriate error
                    view.activity?.toast("Sign In Failed: ${it.exception?.message}")
                }
            }
        }
    }
}