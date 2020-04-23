package com.example.mobileappdev2.start

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger

class StartPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }

    fun doCurrentUser(user: FirebaseUser?) {
        fireStore.fireBaseUser(user)
    }


    fun fetchPosts(){
        fireStore.currentUserId()
        app.fireStore.fetchFavourites{
        }
        fireStore.fetchPosts {
            view.gotoMainPage()
        }
    }
}