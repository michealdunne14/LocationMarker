package com.example.mobileappdev2.post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.CalendarView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.CustomDialog
import com.example.mobileappdev2.adapter.CountryListener
import com.example.mobileappdev2.adapter.DataAdapter
import com.example.mobileappdev2.adapter.ImageAdapter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.models.Location
import com.example.mobileappdev2.models.PostModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import org.jetbrains.anko.*
import java.lang.Exception

class PostView : BaseView(), AnkoLogger, CountryListener {
    var editingPost = false
    lateinit var customDialog: CustomDialog
    lateinit var presenter: PostPresenter

    lateinit var postView: View
    lateinit var dialog: ProgressDialog
    lateinit var map: GoogleMap
    var imagePosition = 0
    var post = PostModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_post, container, false)
        postView = view
        presenter = initPresenter(PostPresenter(this)) as PostPresenter
        dialog = ProgressDialog(context)
        info { "Post Activity Started" }

        view.mPostToolbar.title = getString(R.string.post)
        postView.mPostToolbar.title = getString(R.string.locations_marker)
        (activity as AppCompatActivity?)!!.setSupportActionBar(postView.mPostToolbar)
        setHasOptionsMenu(true)
        view.mPostMap.onCreate(savedInstanceState)

        //Sets up map
        loadMap(view, false)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavHostFragment.findNavController(this@PostView).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, onBackPressedCallback
        )

        view.mPostViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                try {
                    imagePosition = position
                    view.mPostTitle.setText(post.locations[position].title)
                    val loc = LatLng(post.locations[position].latitude, post.locations[position].longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15F))
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

        })

        val postModel = PostViewArgs.fromBundle(arguments!!).postModel
        presenter.postModel = postModel

        if(postModel.fbId.isNotEmpty()){
            info { "Editing Landmark" }
            view.mPostDescription.setText(postModel.description)
            view.mPostSelectCountry.text = postModel.country
            post
            postView.mPostMap.visibility = View.VISIBLE
            val viewPager = view.findViewById<ViewPager>(R.id.mPostViewPager)
            val adapter = ImageAdapter(view.context, postModel.images)
            presenter.setImageArrayListToPostModel(postModel)
            viewPager.adapter = adapter
            editingPost = true
            view.mPostButton.text = getString(R.string.save)
            view.mPostDelete.visibility = View.VISIBLE
        }

        postView.mPostTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(search: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(characterSearch: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!dialog.isShowing) {
                    presenter.doLocationText(imagePosition, characterSearch)
                }
            }
        })

        postView.mPostSelectCountry.setOnClickListener {
            info { "Select Country Started" }
            val dataAdapter = DataAdapter(presenter.getCountryData(), this)
            customDialog = CustomDialog(activity!!, dataAdapter, presenter.fireStore, this)

            customDialog.show()
            customDialog.setCanceledOnTouchOutside(false)
        }

        postView.mPostDelete.setOnClickListener {
            presenter.deletePost()
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
            post.description = mPostDescription.text.toString()
            post.country = mPostSelectCountry.text.toString()
            post.images = presenter.imageArrayList
            post.locations = presenter.locationArrayList
            post.fbId = postModel.fbId
            post.favourite = postModel.favourite
            post.postLiked = postModel.postLiked
            if (date != "") {
                post.datevisted = date
            }
            if (post.images.isNotEmpty() && post.locations.isNotEmpty() && post.description.isNotEmpty() && post.country.isNotEmpty()){
                presenter.createUpdatePost(editingPost,post,postView)
            }else{
                Snackbar.make(postView,"Please Fill in an image and a title", Snackbar.LENGTH_SHORT).show()
                info("Please Fill in an image and a title")
            }
        }

//      Open image gallery
        view.mPostSelectImage.setOnClickListener {
            presenter.selectImages()
        }


        return view
    }

    private fun loadMap(view: View, editing: Boolean) {
        view.mPostMap.getMapAsync {
            map = it
            presenter.initMap(map)
        }
    }

    override fun setImages(imageArrayList: ArrayList<String>) {
        post.locations = presenter.findLocations(map,imagePosition) as ArrayList<Location>
        val viewPager = postView.findViewById<ViewPager>(R.id.mPostViewPager)
        val adapter = ImageAdapter(postView.context, imageArrayList)
        viewPager.adapter = adapter
        if (post.locations.size != 0) {
            postView.mPostTitle.setText(post.locations[imagePosition].title)
        }
    }

    override fun dialogDismiss() {
        dialog.dismiss()
    }

    override fun onCountryClick(string: String) {
        info { "Country Clicked" }
        post.country = string
        mPostSelectCountry.text = string
        customDialog.dismiss()
    }

    override fun navigateUp(){
        postView.findNavController().navigateUp()
    }

    override fun setLocationName(landmarkName: String) {
        postView.mPostTitle.setText(landmarkName)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            dialog = ProgressDialog.show(context, "","Loading. Please wait...",true )
            presenter.doActivityResult(requestCode, resultCode, data, postView.context)
            postView.mPostMap.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        postView.mPostMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        postView.mPostMap.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        postView.mPostMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        postView.mPostMap.onResume()
        loadMap(postView,true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        postView.mPostMap.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                postView.findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.return_menu,menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }
}
