package com.example.mobileappdev2.post

import android.content.Context
import android.content.Intent
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.helper.showImagePicker
import com.example.mobileappdev2.models.PostModel
import org.jetbrains.anko.AnkoLogger

class PostPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    val IMAGE_REQUEST = 1
    var imageArrayList = ArrayList<String>()


    fun selectImages(){
        showImagePicker(view, IMAGE_REQUEST)
    }

    fun setImageArrayListToPostModel(postModel: PostModel){
        imageArrayList = postModel.images
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent, context: Context) {
        when(requestCode){
            IMAGE_REQUEST -> {
                imageArrayList.add(data.data.toString())
                view.setImages(imageArrayList)
            }
        }
    }


    fun findAllImages(): List<String> {
        return imageArrayList
    }
}