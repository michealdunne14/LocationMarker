package com.example.mobileappdev2.base

import android.content.Intent
import com.example.mobileappdev2.MainApp

open class BasePresenter(var view: BaseView)  {

    open var app:MainApp = view.activity?.application as MainApp

    open fun doActivityResult(requestCode: Int,resultCode:Int,data: Intent){
    }

    open fun doRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResult: IntArray){

    }

    open fun onDestrop(){

    }
}