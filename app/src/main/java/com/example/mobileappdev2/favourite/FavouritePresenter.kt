package com.example.mobileappdev2.favourite

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger

class FavouritePresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var user = UserModel()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doGetFavourites(): ArrayList<PostModel> {
        return app.fireStore.favourites
    }
}