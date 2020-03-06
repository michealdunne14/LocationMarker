package com.example.mobileappdev2.firebase

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.navigation.findNavController
import com.example.mobileappdev2.helper.readImageFromPath
import com.example.mobileappdev2.interfacestore.InfoStore
import com.example.mobileappdev2.models.Country
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel
import com.google.android.material.snackbar.Snackbar
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
    val favourites = ArrayList<PostModel>()
    var db: DatabaseReference = FirebaseDatabase.getInstance().reference
    var user: UserModel = UserModel()
    var countries = arrayListOf<Country>()
    var st = FirebaseStorage.getInstance().reference
    var storage = FirebaseStorage.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid


    override fun findAll(): ArrayList<PostModel> {
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
        db.child("users").child(userId).child("posts").addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchFavourites(favouritesReady: () -> Unit){
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("users").child(userId).child("posts")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (posts in dataSnapshot.children) {
                    if (posts.child("favourite").value == true) {
                        favourites.add(posts.getValue<PostModel>(PostModel::class.java)!!)
                    }
                }
                favouritesReady()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun searchCountries(query: CharSequence?): ArrayList<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(postModel: PostModel, view: View) {
        val key = db.child("users").child(user.fbId).child("posts").push().key
        key?.let {
            postModel.fbId = key
            updateImage(postModel,view)
            posts.add(postModel)
        }
    }

    override fun update(postModel: PostModel) {
        val foundPost: PostModel? = posts.find { p -> p.fbId == postModel.fbId }
        if (foundPost != null) {
//            foundPost.titles = postModel.titles
            foundPost.country = postModel.country
            foundPost.datevisted = postModel.datevisted
            foundPost.description = postModel.description
            foundPost.images = postModel.images
        }
        db.child("users").child(userId).child("posts").child(foundPost!!.fbId).setValue(foundPost)
    }

    override fun updateFavourite(postModel: PostModel) {
        if(postModel.favourite){
            favourites.add(postModel)
        }else{
            favourites.remove(postModel)
        }
        db.child("users").child(userId).child("posts").child(postModel.fbId).child("favourite").setValue(postModel.favourite)
    }

    override fun updateLike(postModel: PostModel) {
        db.child("users").child(userId).child("posts").child(postModel.fbId).child("postLiked").setValue(postModel.postLiked)
    }

    override fun createUsers(userModel: UserModel){
        val key:String? = db.child("users").push().key
        key?.let {
            userModel.fbId = key
            user = userModel
            db.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userModel)
        }
    }

    override fun currentUser(): UserModel{
        return user
    }

    fun updateImage(postModel: PostModel, view: View) {
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
                                view.findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun delete(postModel: PostModel) {
        db.child("users").child(userId).child("posts").child(postModel.fbId).removeValue()
        for(image in postModel.images) {
            val photoRef: StorageReference = storage.getReferenceFromUrl(image)
            photoRef.delete().addOnSuccessListener {
                println("File deleted ")
            }.addOnFailureListener {
                // Uh-oh, an error occurred!
                println("File not deleted")
            }
        }
        posts.remove(postModel)
    }

    override fun search(query: CharSequence?, filter: Boolean): ArrayList<PostModel> {
        return posts
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