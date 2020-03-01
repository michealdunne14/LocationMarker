package com.example.mobileappdev2.json

import android.annotation.SuppressLint
import android.content.Context
import com.example.mobileappdev2.interfacestore.InfoStore
import com.example.mobileappdev2.helper.exists
import com.example.mobileappdev2.helper.read
import com.example.mobileappdev2.helper.write
import com.example.mobileappdev2.models.Country
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import kotlin.collections.ArrayList

class MemoryStore(val context: Context): InfoStore,AnkoLogger {

    val landmarks = ArrayList<PostModel>()
    var countries = arrayListOf<Country>()
    var landmarkStore = mutableListOf<PostModel>()

    val JSON_FILE = "MemoryStore.json"
    val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
    val listType = object : TypeToken<ArrayList<PostModel>>() {}.type


    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    private fun serialize() {
        landmarkStore = landmarks
        val jsonString = gsonBuilder.toJson(landmarkStore, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        landmarkStore = Gson().fromJson(jsonString, listType)
        landmarks.addAll(landmarkStore)
    }

    fun generateRandomId(): Long{
        return Random().nextLong()
    }


    override fun findAll(): ArrayList<PostModel> {
        return landmarks
    }

    override fun findPosts(): List<PostModel> {
        return landmarks
    }

    @SuppressLint("DefaultLocale")
    override fun searchCountries(query: CharSequence?): ArrayList<Country> {
        val searchedPosts = ArrayList<Country>()
        for(country in countries){
            if (country.countryName.toUpperCase().contains(query.toString().toUpperCase())){
                searchedPosts.add(country)
            }
        }
        return searchedPosts
    }

    override fun create(postModel: PostModel) {
        postModel.id = generateRandomId()
        landmarks.add(postModel)
        logAll()
        serialize()
    }

    override fun update(postModel: PostModel) {
        val post: PostModel? = landmarks.find { model -> model.id == postModel.id }
        if (post != null){
            post.title = postModel.title
            post.description = postModel.description
            post.images = postModel.images
            logAll()
        }
        serialize()
    }

    override fun delete(postModel: PostModel) {
        val post: PostModel? = landmarks.find { model -> model.id == postModel.id }
        if (post != null){
            landmarks.remove(post)
        }
        serialize()
    }

    override fun search(query: CharSequence?, filter: Boolean): ArrayList<PostModel> {
        val searchedPosts = ArrayList<PostModel>()
        for(marks in landmarks){
            if (marks.title.contains(query!!)){
                if(filter){
                    if(marks.postLiked) {
                        searchedPosts.add(marks)
                    }
                }else{
                    searchedPosts.add(marks)
                }
            }
        }
        return searchedPosts
    }

    fun logAll() {
        landmarks.forEach{ info("${it}") }
    }

    override fun getCountryData(): ArrayList<Country> {
        return countries
    }

    override fun createUsers(userModel: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun preparedata() {
        for (countryCode in Locale.getISOCountries()) {
            val locale = Locale("",countryCode)
            var countryName: String? = locale.displayCountry
            if (countryName == null) {
                countryName = "UnIdentified"
            }
            val simpleCountry = Country(countryName, countryCode)
            countries.add(simpleCountry)
        }
        countries = ArrayList(countries.sortedWith(compareBy { it.countryName }))
    }

}