package com.example.mobileappdev2.post

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.CustomDialog
import com.example.mobileappdev2.adapter.CountryListener
import com.example.mobileappdev2.adapter.DataAdapter
import com.example.mobileappdev2.adapter.ImageAdapter
import com.example.mobileappdev2.helper.showImagePicker
import com.example.mobileappdev2.models.PostModel
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.main_layout.view.*
import org.jetbrains.anko.*
import java.util.ArrayList

class PostFragment : Fragment(), AnkoLogger, CountryListener {
    var postModel = PostModel()
    lateinit var app : MainApp
    private val imageList = ArrayList<String>()
    var editingPost = false
    lateinit var customDialog: CustomDialog
    val IMAGE_REQUEST = 1

    lateinit var postView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_post, container, false)
        postView = view
        app = activity!!.application as MainApp
        info { "Post Activity Started" }

        view.mPostToolbar.title = getString(R.string.post)
        postView.mPostToolbar.title = getString(R.string.locations_marker)
        (activity as AppCompatActivity?)!!.setSupportActionBar(postView.mPostToolbar)
        setHasOptionsMenu(true)

        val postModel = PostFragmentArgs.fromBundle(arguments!!).postModel

        if(postModel.title != ""){
            info { "Editing Landmark" }
            mPostTitle.setText(postModel.title)
            mPostDescription.setText(postModel.description)
            mPostSelectCountry.text = postModel.country
            val viewPager = view.findViewById<ViewPager>(R.id.mPostViewPager)
            imageList.addAll(postModel.images)
            val adapter = ImageAdapter(view.context, postModel.images)
            viewPager.adapter = adapter
            editingPost = true
            mPostButton.text = getString(R.string.save)
            mPostDelete.visibility = View.VISIBLE
        }

        postView.mPostSelectCountry.setOnClickListener {
            info { "Select Country Started" }
            val dataAdapter = DataAdapter(app.fireStore.getCountryData(), this)
            customDialog = CustomDialog(activity!!, dataAdapter, app.fireStore, this)

            customDialog.show()
            customDialog.setCanceledOnTouchOutside(false)
        }

        postView.mPostDelete.setOnClickListener {
            doAsync {
                app.fireStore.delete(postModel)
                info { "Delete Landmark $postModel" }
                onComplete {
                    view.findNavController().navigateUp()
                }
            }
        }

        var date = String()
        //sets the date of the calender when changed.
        postView.mPostVisited.setOnDateChangeListener(CalendarView.OnDateChangeListener(){
                view, year, month, dayOfMonth ->
            date = "$dayOfMonth/$month/$year"
        })

        //      Updates or creates a post
        postView.mPostButton.setOnClickListener {
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
                    if (editingPost) {
                        app.fireStore.update(postModel.copy())
                    } else {
                        app.fireStore.create(postModel.copy())
                    }
                    onComplete {
                        info { "Created Landmark: ${postModel}" }
                        view.findNavController().navigateUp()
                    }
                }
            }else{
                info("Please Fill in an image and a title")
            }
        }

//      Open image gallery
        view.mPostSelectImage.setOnClickListener {
            showImagePicker(activity!!, IMAGE_REQUEST)
        }


        return view
    }


    override fun onCountryClick(string: String) {
        info { "Country Clicked" }
        postModel.country = string
        mPostSelectCountry.text = string
        customDialog.dismiss()
    }
}
