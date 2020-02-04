package com.example.mobileappdev2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_post.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.ArrayList

class PostActivity : AppCompatActivity(),AnkoLogger {

    var postModel = PostModel()
    lateinit var app : MainApp
    private val imageList = ArrayList<String>()

    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        app = application as MainApp

        mPostButton.setOnClickListener {
            postModel.title = mPostTitle.text.toString()
            postModel.description = mPostDescription.text.toString()
            postModel.images.addAll(imageList)
            if (postModel.title.isNotEmpty()){
                app.landmarks.create(postModel)
                info { "add Button Pressed: ${postModel}" }
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        mPostSelectImage.setOnClickListener {
            showImagePicker(this,IMAGE_REQUEST)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_REQUEST -> {
                if (data != null){
//                    mHillFortImage.setImageBitmap(readImage(this,resultCode,data))
                    imageList.add(data.data.toString())
                    val viewPager = findViewById<ViewPager>(R.id.mPostViewPager)
                    val adapter = ImageAdapter(this,imageList)
                    viewPager.adapter = adapter
                }
            }
        }
    }
}
