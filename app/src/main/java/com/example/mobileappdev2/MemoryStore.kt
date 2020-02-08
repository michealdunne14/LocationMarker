package com.example.mobileappdev2

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.nio.file.Files.exists
import java.util.*
import kotlin.collections.ArrayList

class MemoryStore(val context: Context): PostStore,AnkoLogger {

    val landmarks = ArrayList<PostModel>()
    var normalArray = arrayListOf<Country>()
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

    override fun search(query: CharSequence?): ArrayList<PostModel> {
        val searchedPosts = ArrayList<PostModel>()
        for(marks in landmarks){
            if (marks.title.contains(query!!)){
                searchedPosts.add(marks)
            }
        }
        return searchedPosts
    }

    fun logAll() {
        landmarks.forEach{ info("${it}") }
    }

    override fun getCountryData(): ArrayList<Country> {
        return normalArray
    }

    override fun preparedata() {
        for (countryCode in Locale.getISOCountries()) {
            val locale = Locale("",countryCode)
            var countryName: String? = locale.displayCountry
            if (countryName == null) {
                countryName = "UnIdentified"
            }
            val simpleCountry = Country(countryName,countryCode)
            normalArray.add(simpleCountry)
        }
        normalArray = ArrayList(normalArray.sortedWith(compareBy { it.countryName }))
    }

}