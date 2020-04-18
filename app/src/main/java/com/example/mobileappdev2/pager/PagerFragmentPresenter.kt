package com.example.mobileappdev2.pager

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import org.jetbrains.anko.AnkoLogger

class PagerFragmentPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    override var app : MainApp = view.activity?.application as MainApp
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }
}