package com.example.mobileappdev2.fragment.home

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.example.mobileappdev2.models.PostModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync

class HomePresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }


    fun doPrepareData(){
        doAsync {
            fireStore.preparedata()
        }
    }

//  Find all posts
    fun findAll(): ArrayList<PostModel> {
        return fireStore.findAll()
    }

    override fun updateFavourite(postModel: PostModel){
        doAsync {
            fireStore.updateFavourite(postModel)
        }
    }

    fun homeSearch(
        characterSearch: CharSequence?,
        filter: Boolean
    ) {
        doAsync {
            fireStore.search(characterSearch, filter,view)
        }
    }

    override fun updateLike(postModel: PostModel){
        doAsync {
            fireStore.updateLike(postModel)
        }
    }
}