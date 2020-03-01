package com.example.mobileappdev2.firebase

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import com.example.mobileappdev2.helper.readImageFromPath
import com.example.mobileappdev2.interfacestore.InfoStore
import com.example.mobileappdev2.models.Country
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class FireStore(val context: Context): InfoStore, AnkoLogger {

    val posts = ArrayList<PostModel>()
    var db: DatabaseReference = FirebaseDatabase.getInstance().reference
    var user: UserModel = UserModel()
    var countries = arrayListOf<Country>()
    var st = FirebaseStorage.getInstance().reference
    val userId = FirebaseAuth.getInstance().currentUser!!.uid


    override fun findAll(): List<PostModel> {
        return posts
    }

    override fun findPosts(): List<PostModel> {
        return posts
    }

    fun fetchPosts(postsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(posts) { it.getValue<PostModel>(PostModel::class.java) }
                postsReady()
            }
        }
        db.child("users").child(userId).child("posts").addValueEventListener(valueEventListener)
    }

    override fun searchCountries(query: CharSequence?): ArrayList<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(postModel: PostModel) {
        val key = db.child("users").child(user.fbId).child("posts").push().key
        key?.let {
            postModel.fbId = key
            updateImage(postModel)
            posts.add(postModel)
        }
    }

    override fun update(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUsers(userModel: UserModel){
        val key:String? = db.child("users").push().key
        key?.let {
            userModel.fbId = key
            user = userModel
            db.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userModel)
        }
    }

    fun updateImage(postModel: PostModel) {
        val postImageArrayList = ArrayList<String>()
        postImageArrayList.addAll(postModel.images)
        postModel.images.clear()
        for (image in postImageArrayList) {
            if (image != "") {
                val fileName = File(image)
                val imageName = fileName.name

                val imageRef = st.child(user.fbId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, image)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)

                    uploadTask.addOnFailureListener {
                        println(it)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            postModel.images.add(it.toString())
                            if (postModel.images.size == postImageArrayList.size) {
                                db.child("users").child(userId).child("posts").child(postModel.fbId).setValue(postModel)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun delete(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun search(query: CharSequence?, filter: Boolean): ArrayList<PostModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun preparedata() {
        for (countryCode in Locale.getISOCountries()) {
            val locale = Locale("",countryCode)
            var countryName: String? = locale.displayCountry
            if (countryName == null) {
                countryName = "UnIdentified"
            }
            val simpleCountry = Country(
                countryName,
                countryCode
            )
            countries.add(simpleCountry)
        }
        countries = ArrayList(countries.sortedWith(compareBy { it.countryName }))
    }

    override fun getCountryData(): ArrayList<Country> {
        return countries
    }

}