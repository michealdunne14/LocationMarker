package com.example.mobileappdev2.post

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.helper.readImageFromPath
import com.example.mobileappdev2.helper.showImagePicker
import com.example.mobileappdev2.models.Location
import com.example.mobileappdev2.models.PostModel
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import org.jetbrains.anko.AnkoLogger

class PostPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    val IMAGE_REQUEST = 1
    var imageArrayList = ArrayList<String>()
    var locationArrayList = ArrayList<Location>()


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


    fun searchLandmark(imageArrayList: ArrayList<String>) {
        locationArrayList.clear()
        for(imageString in imageArrayList){
            val bitmap = readImageFromPath(view.context!!, imageString)
            val image = FirebaseVisionImage.fromBitmap(bitmap!!)
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
    }


    fun findAllImages(): List<String> {
        return imageArrayList
    }

    fun findLocations(): List<Location>{
        return locationArrayList
    }
}