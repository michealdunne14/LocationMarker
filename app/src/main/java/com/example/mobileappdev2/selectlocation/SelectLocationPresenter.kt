package com.example.mobileappdev2.selectlocation

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.example.mobileappdev2.helper.isPermissionGranted
import com.example.mobileappdev2.models.Location
import com.example.mobileappdev2.models.PostModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.AnkoLogger

class SelectLocationPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var postModel = PostModel()
    var imagePosition = 0
    var defaultLocation = Location("Default Location",52.245696, -7.139102)
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.activity!!)
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }



    lateinit var googleMap: GoogleMap

    fun initMap(
        map: GoogleMap,
        post: PostModel,
        position: Int
    ){
        map.uiSettings.isZoomControlsEnabled = true
        googleMap = map
        postModel = post
        imagePosition = position
        if (post.images.size != 0){
            val loc = LatLng(post.locations[position].latitude, post.locations[position].longitude)
            val options = MarkerOptions()
                .title("Location")
                .snippet("GPS : " + loc.toString())
                .draggable(true)
                .position(loc)
            googleMap.addMarker(options)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15F))
        }else{
            doSetCurrentLocation()
        }
    }

    //  Update Location to current location
    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            // permissions denied, so use the default location
            locationUpdate(defaultLocation.latitude, defaultLocation.longitude)
        }
    }

    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    //  Set the location of the map
    fun locationUpdate(lat: Double, lng: Double) {
        googleMap.clear()
        googleMap.uiSettings.isZoomControlsEnabled = true
        val options = MarkerOptions().title("Location").draggable(true).position(LatLng(lat,lng))
        googleMap.addMarker(options)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat,lng), 15F))
    }

    fun doUpdateLocation(location: LatLng) {
        fireStore.latLng = location
    }
}