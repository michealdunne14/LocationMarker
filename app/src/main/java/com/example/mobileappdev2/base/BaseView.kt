package com.example.mobileappdev2.base

import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger

enum class VIEW {
    MAIN,START
}

open abstract class BaseView: Fragment(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    open fun doSetDetails(email: String,password: String,name: String) {}
    open fun showFloatingAction(){}
    open fun hideFloatingAction(){}
    open fun gotoMainPage(){}
    open fun gotoMainPageFromRegister(){}
    open fun setImages(imageArrayList: ArrayList<String>) {}
}