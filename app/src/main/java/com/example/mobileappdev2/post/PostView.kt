package com.example.mobileappdev2.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.CustomDialog
import com.example.mobileappdev2.adapter.CountryListener
import com.example.mobileappdev2.adapter.DataAdapter
import com.example.mobileappdev2.adapter.ImageAdapter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.models.PostModel
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import org.jetbrains.anko.*

class PostView : BaseView(), AnkoLogger, CountryListener {
    var postModel = PostModel()
    lateinit var app : MainApp
    var editingPost = false
    lateinit var customDialog: CustomDialog
    lateinit var presenter: PostPresenter

    lateinit var postView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_post, container, false)
        postView = view
        presenter = initPresenter(PostPresenter(this)) as PostPresenter

        app = activity!!.application as MainApp
        info { "Post Activity Started" }

        view.mPostToolbar.title = getString(R.string.post)
        postView.mPostToolbar.title = getString(R.string.locations_marker)
        (activity as AppCompatActivity?)!!.setSupportActionBar(postView.mPostToolbar)
        setHasOptionsMenu(true)

        val postModel = PostViewArgs.fromBundle(arguments!!).postModel

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavHostFragment.findNavController(this@PostView).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, onBackPressedCallback
        )

        if(postModel.title.isNotEmpty()){
            info { "Editing Landmark" }
            view.mPostTitle.setText(postModel.title)
            view.mPostDescription.setText(postModel.description)
            view.mPostSelectCountry.text = postModel.country
            val viewPager = view.findViewById<ViewPager>(R.id.mPostViewPager)
            val adapter = ImageAdapter(view.context, postModel.images)
            presenter.setImageArrayListToPostModel(postModel)
            viewPager.adapter = adapter
            editingPost = true
            view.mPostButton.text = getString(R.string.save)
            view.mPostDelete.visibility = View.VISIBLE
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
            if (postModel.title.isNotEmpty()){
                doAsync {
                    if (editingPost) {
                        app.fireStore.update(postModel.copy())
                    } else {
                        postModel.images = presenter.findAllImages() as ArrayList<String>
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
            presenter.selectImages()
        }


        return view
    }

    override fun setImages(imageArrayList: ArrayList<String>) {
        val viewPager = postView.findViewById<ViewPager>(R.id.mPostViewPager)
        val adapter = ImageAdapter(postView.context, imageArrayList)
        viewPager.adapter = adapter
    }


    override fun onCountryClick(string: String) {
        info { "Country Clicked" }
        postModel.country = string
        mPostSelectCountry.text = string
        customDialog.dismiss()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data, postView.context)
        }

    }
}
