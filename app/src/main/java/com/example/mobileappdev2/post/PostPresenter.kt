package com.example.mobileappdev2.post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.example.mobileappdev2.helper.isPermissionGranted
import com.example.mobileappdev2.helper.readImageFromPath
import com.example.mobileappdev2.helper.showImagePicker
import com.example.mobileappdev2.models.Country
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
import kotlinx.android.synthetic.main.fragment_post.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.onComplete
import kotlin.collections.ArrayList


var imageArrayList = ArrayList<String>()
var locationArrayList = ArrayList<Location>()

class PostPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    val IMAGE_REQUEST = 1
    lateinit var googleMap: GoogleMap
    var postModel = PostModel()
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.context!!)
    var defaultLocation = Location("",52.245696, -7.139102)
    var fireStore: FireStore
    var editing = false


    init {
        fireStore = app.fireStore as FireStore
    }

    fun selectImages(){
        showImagePicker(view, IMAGE_REQUEST)
    }

    fun setImageArrayListToPostModel(postModel: PostModel){
        imageArrayList = postModel.images
        locationArrayList = postModel.locations
    }

    fun deletePost(){
        doAsync {
            fireStore.delete(postModel)
            info { "Delete Landmark $postModel" }
            onComplete {
                view.navigateUp()
            }
        }
    }


    fun createUpdatePost(
        editingPost: Boolean,
        post: PostModel,
        view: View
    ) {
        doAsync {
            if (editingPost) {
                app.fireStore.update(post.copy(), view)
            } else {
                app.fireStore.create(post.copy(), view)
            }
        }
    }


    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent, context: Context) {
        when(requestCode){
            IMAGE_REQUEST -> {
                imageArrayList.add(data.data.toString())
                searchLandmark(imageArrayList)
            }
        }
    }

    fun getCountryData(): ArrayList<Country> {
        return fireStore.getCountryData()
    }

    fun initMap(map: GoogleMap){
        map.uiSettings.isZoomControlsEnabled = true
        googleMap = map
        view.setImages(imageArrayList)
    }


    fun searchLandmark(imageArrayList: ArrayList<String>) {
        locationArrayList.clear()
        val bitmapImagesArrayList = ArrayList<Bitmap>()
        imageArrayList.forEachIndexed { index, imageString ->
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
                        bitmapImagesArrayList.add(bitmap)
                        if (imageArrayList.size == bitmapImagesArrayList.size){
                            bitmapImagesArrayList.reverse()
                            for (image in bitmapImagesArrayList) {
                                loadingImages(image, index)
                            }
                        }
                    }
                })
            }else{
                bitmap = readImageFromPath(view.context!!, imageString)!!
                bitmapImagesArrayList.add(bitmap)

                if (imageArrayList.size == bitmapImagesArrayList.size){
                    for (image in bitmapImagesArrayList) {
                        loadingImages(image, index)
                    }
                }
            }
        }
    }

    fun loadingImages(
        bitmap: Bitmap,
        index: Int) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().visionCloudLandmarkDetector
        val result = detector.detectInImage(image)
        result.addOnSuccessListener { firebaseVisionCloudLandmarks ->
            for (landmark in firebaseVisionCloudLandmarks) {
                val confidence = landmark.confidence
                val landmarkName = landmark.landmark

                if (landmark.locations.isNotEmpty()){
                    view.hideCustomButton()
                }

                for (loc in landmark.locations) {
                    val latitude = loc.latitude
                    val longitude = loc.longitude
                    locationArrayList.add(Location(landmarkName,latitude,longitude))
                    break
                }

                break
            }
        }
            .addOnCompleteListener{ task ->
                view.setImages(imageArrayList)
                view.dialogDismiss()
            }
            .addOnFailureListener { e ->

            }
    }

    fun doLocationText(imagePosition: Int, characterSearch: CharSequence?) {
        if(locationArrayList.size != 0) {
            locationArrayList[imagePosition].title = characterSearch.toString()
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

    private fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    //  Set the location of the map
    private fun locationUpdate(lat: Double, lng: Double) {
        postModel.locations[0].longitude = lng
        postModel.locations[0].latitude = lat
        googleMap.clear()
        googleMap.uiSettings?.isZoomControlsEnabled = true
        val options = MarkerOptions().position(LatLng(postModel.locations[0].latitude,postModel.locations[0].longitude))
        googleMap.addMarker(options)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(postModel.locations[0].latitude,postModel.locations[0].longitude),15F))
    }


    fun customLocation(view: View, imagePosition: Int) {
        if (fireStore.latLng != null){
            if (locationArrayList.isNotEmpty()) {
                locationArrayList.removeAt(imagePosition)
            }
            locationArrayList.add(Location("",fireStore.latLng!!.latitude,fireStore.latLng!!.longitude))
            fireStore.latLng = null
            view.mPostMap.visibility = View.VISIBLE
        }
    }


    fun findLocations(map: GoogleMap, imagePosition: Int): List<Location>{
        locationArrayList.forEach {
                val loc = LatLng(it.latitude, it.longitude)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it
        }
        if (locationArrayList.size != 0 && locationArrayList.size > imagePosition) {
            val loc = LatLng(
                locationArrayList[imagePosition].latitude,
                locationArrayList[imagePosition].longitude
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15F))
        }
        return locationArrayList
    }
}