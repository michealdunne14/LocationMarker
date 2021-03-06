package com.example.mobileappdev2.firebase

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.navigation.findNavController
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.helper.readImageFromPath
import com.example.mobileappdev2.interfacestore.InfoStore
import com.example.mobileappdev2.models.Country
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class FireStore(val context: Context): InfoStore, AnkoLogger {

    val posts = ArrayList<PostModel>()
    val searchedPosts = ArrayList<PostModel>()
    val favourites = ArrayList<PostModel>()
    var db: DatabaseReference = FirebaseDatabase.getInstance().reference
    var user: UserModel = UserModel()
    var countries = arrayListOf<Country>()
    var st = FirebaseStorage.getInstance().reference
    var storage = FirebaseStorage.getInstance()
    var userId = ""
    var totalUsers: Long = 0
    var totalPosts: Long = 0
    var totalLikes: Long = 0
    var totalFavourites: Long = 0
    var postModel = PostModel()
    var latLng: LatLng? = null


    //  Find all users posts
    override fun findAll(): ArrayList<PostModel> {
        return posts
    }

    //  Find all favorites
    override fun findFavourites(): ArrayList<PostModel> {
        return favourites
    }

    fun currentUserId(): String {
        try {
            userId = FirebaseAuth.getInstance().currentUser!!.uid
        }catch (e : Exception){
            e.printStackTrace()
        }
        return userId
    }

    //  Fetch Posts belong to that user
    fun fetchPosts(postsReady: () -> Unit) {
        totalLikes = 0
        totalFavourites = 0
        totalPosts = 0
        totalUsers = 0
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(posts) { it.getValue<PostModel>(PostModel::class.java) }
                postsReady()
            }
        }
        db.child("users").child(userId).child("posts").addListenerForSingleValueEvent(valueEventListener)

        val statsValueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                totalUsers = dataSnapshot.childrenCount
                for(count in dataSnapshot.children){
                    totalPosts = count.child("posts").childrenCount
                    for (post in count.child("posts").children){
                        if (post.child("postLiked").value == true){
                            totalLikes++
                        }
                        if (post.child("favourite").value == true){
                            totalFavourites++
                        }
                    }
                }
            }
        }

        db.child("users").addListenerForSingleValueEvent(statsValueEventListener)
    }

    //  Fetch all favourite posts
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

    //  Search by country
    override fun searchCountries(query: CharSequence?): ArrayList<Country> {
        val searchedPosts = ArrayList<Country>()
        for(country in countries){
            if (country.countryName.toUpperCase().contains(query!!)){
                searchedPosts.add(country)
            }
        }
        return searchedPosts
    }

    //  Create a post and upload the images
    override fun create(postModel: PostModel, view: View) {
        val key = db.child("users").child(user.fbId).child("posts").push().key
        totalPosts++
        key?.let {
            postModel.fbId = key
            posts.add(postModel)
            updateImage(postModel, false,view)
        }
    }

    //  Update the posts
    override fun update(postModel: PostModel,view: View) {
        val foundPost: PostModel? = posts.find { p ->
            p.fbId == postModel.fbId
        }
        if (foundPost != null) {
            foundPost.country = postModel.country
            foundPost.datevisted = postModel.datevisted
            foundPost.description = postModel.description
            updateImage(postModel, true, view)
        }
        db.child("users").child(userId).child("posts").child(postModel.fbId).child("locations").setValue(postModel.locations)
        db.child("users").child(userId).child("posts").child(postModel.fbId).child("country").setValue(postModel.country)
        db.child("users").child(userId).child("posts").child(postModel.fbId).child("description").setValue(postModel.description)
//        db.child("users").child(userId).child("posts").child(foundPost!!.fbId).setValue(foundPost)
    }

    //  Update the favorites List
    override fun updateFavourite(postModel: PostModel) {
        if(postModel.favourite){
            postModel.favourite = true
            favourites.add(postModel)
        }else{
            postModel.favourite = false
            favourites.remove(postModel)
        }
        totalFavourites++
        db.child("users").child(userId).child("posts").child(postModel.fbId).child("favourite").setValue(postModel.favourite)
    }

    //  Update Like
    override fun updateLike(postModel: PostModel) {
        db.child("users").child(userId).child("posts").child(postModel.fbId).child("postLiked").setValue(postModel.postLiked)
    }
    //  Clear data
    fun clearData(){
        userId = ""
        posts.clear()
        favourites.clear()
        user = UserModel()
    }

    fun clearPosts(){
        posts.clear()
        favourites.clear()
    }

    //  Create a user
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
    //  Get the firebase User
    fun fireBaseUser(user: FirebaseUser?): FirebaseUser? {
        return user
    }
    //  add image to post
    fun updateImage(
        postModel: PostModel,
        update: Boolean,
        view: View
    ) {
        val postImageArrayList = ArrayList<String>()
        postImageArrayList.addAll(postModel.images)
        val removingImage =ArrayList<String>()
        for (image in postImageArrayList){
            if (!image.contains("https://firebasestorage")){
                removingImage.add(image)
            }
        }

        if (removingImage.size == 0 && update){
            view.findNavController().navigateUp()
        }

        removingImage.forEachIndexed { index, image ->
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
                            postImageArrayList.add(it.toString())
                            postModel.images.add(it.toString())
                            if (index == removingImage.size -1 ) {
                                if (update){
                                    val updateImage = ArrayList<String>()
                                    for (findImage in postImageArrayList){
                                        if (findImage.contains("https://firebasestorage.googleapis")){
                                            updateImage.add(findImage)
                                        }
                                    }
                                    db.child("users").child(userId).child("posts").child(postModel.fbId).child("images").setValue(updateImage)
                                }else {
                                    for (findImage in postImageArrayList){
                                        if (!findImage.contains("https://firebasestorage.googleapis")){
                                            postModel.images.remove(findImage)
                                        }
                                    }
                                    db.child("users").child(userId).child("posts").child(postModel.fbId).setValue(postModel)
                                }
                                view.findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
    //  Delete a post
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
    //  Search for a posts
    override fun search(
        query: CharSequence?,
        filter: Boolean,
        view: BaseView
    ) {
        searchedPosts.clear()
        val reference = FirebaseDatabase.getInstance().reference
        val postsSearch = reference.child("users").child(userId).child("posts")
        postsSearch.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (posts in dataSnapshot.children) {
                    val postModel = posts.getValue(PostModel::class.java)!!
                    for (location in postModel.locations){
                        if(filter){
                            if (location.title.contains(query!!) && postModel.favourite) {
                                searchedPosts.add(postModel)
                                break
                            }
                        }else {
                            if (location.title.contains(query!!)) {
                                searchedPosts.add(postModel)
                                break
                            }
                        }
                    }
                    view.searchLandmarks(findSearchedPosts())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun findSearchedPosts():ArrayList<PostModel>{
        return searchedPosts
    }


    //  add countries to an arraylist
    override fun preparedata() {
        countries.clear()
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