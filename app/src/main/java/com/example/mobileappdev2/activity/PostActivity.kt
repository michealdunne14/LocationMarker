package com.example.mobileappdev2.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.CountryListener
import com.example.mobileappdev2.adapter.DataAdapter
import com.example.mobileappdev2.adapter.ImageAdapter
import com.example.mobileappdev2.helper.showImagePicker
import com.example.mobileappdev2.models.PostModel
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_post.mPostToolbar
import org.jetbrains.anko.*
import java.util.ArrayList

class PostActivity : AppCompatActivity(),AnkoLogger, CountryListener {
    var postModel = PostModel()
    lateinit var app : MainApp
    private val imageList = ArrayList<String>()
    var editingPost = false
    lateinit var customDialog: CustomDialog

    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        app = application as MainApp
        info { "Post Activity Started" }

        mPostToolbar.title = getString(R.string.post)
        setSupportActionBar(mPostToolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if(intent.hasExtra("landmark_edit")){
            info { "Editing Landmark" }
            postModel = intent.extras?.getParcelable<PostModel>("landmark_edit")!!
            mPostTitle.setText(postModel.title)
            mPostDescription.setText(postModel.description)
            mPostSelectCountry.text = postModel.country
            val viewPager = findViewById<ViewPager>(R.id.mPostViewPager)
            imageList.addAll(postModel.images)
            val adapter = ImageAdapter(
                this,
                postModel.images
            )
            viewPager.adapter = adapter
            editingPost = true
            mPostButton.text = getString(R.string.save)
            mPostDelete.visibility = View.VISIBLE
        }

        mPostSelectCountry.setOnClickListener {
            info { "Select Country Started" }
            val dataAdapter = DataAdapter(
                app.fireStore.getCountryData(),
                this
            )
            customDialog = CustomDialog(
                this,
                dataAdapter,
                app.fireStore,
                this
            )

            customDialog.show()
            customDialog.setCanceledOnTouchOutside(false)
        }

        mPostDelete.setOnClickListener {
            doAsync {
                app.fireStore.delete(postModel)
                info { "Delete Landmark $postModel" }
                onComplete {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
        var date = String()
        //sets the date of the calender when changed.
        mPostVisited.setOnDateChangeListener(CalendarView.OnDateChangeListener(){
                view, year, month, dayOfMonth ->
            date = "$dayOfMonth/$month/$year"
        })

//      Updates or creates a post
        mPostButton.setOnClickListener {
            info { "Posting Landmark" }
            postModel.images = ArrayList()
            postModel.title = mPostTitle.text.toString()
            postModel.description = mPostDescription.text.toString()
            postModel.country = mPostSelectCountry.text.toString()
            if (date != "") {
                postModel.datevisted = date
            }
            (postModel.images as ArrayList<String>).addAll(imageList)
            if (postModel.title.isNotEmpty() && postModel.images.isNotEmpty()){
                doAsync {
//                  Update editing landmark
                    if (editingPost) {
                        app.fireStore.update(postModel.copy())
                    } else {
                        app.fireStore.create(postModel.copy())
                    }
                    onComplete {
                        info { "Created Landmark: ${postModel}" }
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }else{
                toast("Please Fill in an image and a title")
            }
        }

//      Open image gallery
        mPostSelectImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }
    }

//  Back button Pressed
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_REQUEST -> {
                info { "Image Request" }
                if (data != null){
                    imageList.add(data.data.toString())
                    val viewPager = findViewById<ViewPager>(R.id.mPostViewPager)
                    val adapter = ImageAdapter(this, imageList)
                    viewPager.adapter = adapter
                }
            }
        }
    }

    override fun onCountryClick(string: String) {
        info { "Country Clicked" }
        postModel.country = string
        mPostSelectCountry.text = string
        customDialog.dismiss()
    }
}
