package com.example.mobileappdev2.maps

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.example.mobileappdev2.models.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

class MapsPresenter(view: BaseView): BasePresenter(view) {
    override var app : MainApp = view.activity?.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }

    fun initMap(map: GoogleMap){
        map.uiSettings.isZoomControlsEnabled = true
        findLocations(map)
    }

    fun findLocations(map: GoogleMap){
        val findPosts = fireStore.findFavourites()
        findPosts.forEach { postModel ->
            postModel.locations.forEach {
                val loc = LatLng(it.latitude, it.longitude)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it
                map.moveCamera(CameraUpdateFactory.newLatLng(loc))
            }
        }
    }

    fun doMarkerClick(title: String?): Location? {
        val findPosts = fireStore.findFavourites()
        var location: Location?
        for (post in findPosts){
            location = post.locations.find { p -> p.title == title }
            val index = post.locations.indexOf(location)
            if(location != null){
                view.markerLocations(location,post,index)
            }
        }
        return null
    }
}