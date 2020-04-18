package com.example.mobileappdev2.post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.helper.isPermissionGranted
import com.example.mobileappdev2.helper.readImageFromPath
import com.example.mobileappdev2.helper.showImagePicker
import com.example.mobileappdev2.models.Location
import com.example.mobileappdev2.models.PostModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import org.jetbrains.anko.AnkoLogger
import java.util.*
import kotlin.collections.ArrayList

class PostPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    val IMAGE_REQUEST = 1
    lateinit var googleMap: GoogleMap
    var imageArrayList = ArrayList<String>()
    var locationArrayList = ArrayList<Location>()
    var postModel = PostModel()
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.context!!)
    var defaultLocation = Location("",52.245696, -7.139102)



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
                searchLandmark(imageArrayList)
            }
        }
    }

    fun initMap(map: GoogleMap){
        map.uiSettings.isZoomControlsEnabled = true
        googleMap = map
        findLocations(map)
    }


    fun searchLandmark(imageArrayList: ArrayList<String>) {
        locationArrayList.clear()
        for(imageString in imageArrayList){
            lateinit var bitmap: Bitmap
            if(imageString.contains("https://firebasestorage.googleapis")){
                Glide.with(view.context!!).asBitmap().load(imageString).into(object : CustomTarget<Bitmap>(){
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        bitmap = resource
                        loadingImages(bitmap)
                    }
                })
            }else{
                bitmap = readImageFromPath(view.context!!, imageString)!!
                loadingImages(bitmap)
            }
        }
    }

    fun loadingImages(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().visionCloudLandmarkDetector
        val result = detector.detectInImage(image)
        result.addOnSuccessListener { firebaseVisionCloudLandmarks ->
            for (landmark in firebaseVisionCloudLandmarks) {
                val confidence = landmark.confidence
                val landmarkName = landmark.landmark

                for (loc in landmark.locations) {
                    val latitude = loc.latitude
                    val longitude = loc.longitude
                    view.setLocation(landmarkName,latitude,longitude)
                    locationArrayList.add(Location(landmarkName,latitude,longitude))
                    locationArrayList.reverse()
                    break
                }
                break
            }
        }
            .addOnCompleteListener{ task ->
                view.setImages(imageArrayList)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }


    fun findAllImages(): ArrayList<String> {
        return imageArrayList
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

    private fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    //  Set the location of the map
    fun locationUpdate(lat: Double, lng: Double) {
        postModel.locations[0].longitude = lng
        postModel.locations[0].latitude = lat
        googleMap.clear()
        googleMap.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().position(LatLng(postModel.locations[0].latitude,postModel.locations[0].longitude))
        googleMap.addMarker(options)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(postModel.locations[0].latitude,postModel.locations[0].longitude),15F))
    }


    fun findLocations(map: GoogleMap): List<Location>{
        locationArrayList.forEach {
                val loc = LatLng(it.latitude, it.longitude)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15F))
        }
        return locationArrayList
    }

    fun locationArray(): ArrayList<Location> {
        return locationArrayList
    }
}