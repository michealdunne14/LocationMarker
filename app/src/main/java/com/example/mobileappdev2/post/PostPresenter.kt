package com.example.mobileappdev2.post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.helper.readImageFromPath
import com.example.mobileappdev2.helper.showImagePicker
import com.example.mobileappdev2.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class PostPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    val IMAGE_REQUEST = 1
    val imageArrayList = ArrayList<String>()


    fun selectImages(){
        showImagePicker(view, IMAGE_REQUEST)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent, context: Context) {
        when(requestCode){
            IMAGE_REQUEST -> {
                imageArrayList.add(data.data.toString())
            }
        }
    }


    fun findAllImages(): List<String> {
        return imageArrayList
    }
}