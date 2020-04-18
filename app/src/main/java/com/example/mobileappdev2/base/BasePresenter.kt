package com.example.mobileappdev2.base

import android.content.Context
import android.content.Intent
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.models.PostModel

open class BasePresenter(var view: BaseView)  {

    open var app:MainApp = view.activity?.application as MainApp

    open fun doActivityResult(requestCode: Int,resultCode:Int,data: Intent,context: Context){
    }

    open fun doRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResult: IntArray){

    }

    open fun onDestrop(){

    }

    open fun updateFavourite(postModel: PostModel) {}
    open fun updateLike(postModel: PostModel) {}
}