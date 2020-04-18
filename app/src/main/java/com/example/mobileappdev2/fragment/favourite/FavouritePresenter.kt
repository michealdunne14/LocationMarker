package com.example.mobileappdev2.fragment.favourite

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync

class FavouritePresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var user = UserModel()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }


    fun doGetFavourites(): ArrayList<PostModel> {
        return fireStore.favourites
    }


    override fun updateFavourite(postModel: PostModel){
        doAsync {
            fireStore.updateFavourite(postModel)
        }
    }

    override fun updateLike(postModel: PostModel){
        doAsync {
            fireStore.updateLike(postModel)
        }
    }
}