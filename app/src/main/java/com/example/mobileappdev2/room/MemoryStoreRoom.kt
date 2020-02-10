package com.example.mobileappdev2.room

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.interfacestore.PostStore
import com.example.mobileappdev2.models.Country
import org.jetbrains.anko.AnkoLogger
import java.util.*
import kotlin.collections.ArrayList

class MemoryStoreRoom(val context: Context): PostStore, AnkoLogger {

    var dao: PostDao
    var countries = arrayListOf<Country>()
    var posts: List<PostModel> = ArrayList()


    init {
        val database = Room.databaseBuilder(context, Database::class.java, "landmarks_database.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.postDao()
    }

//  Find all Landmarks
    override fun findAll(): List<PostModel> {
        posts = dao.findAll()
        return posts
    }
//  Find Posts
    override fun findPosts(): List<PostModel>{
        return posts
    }
//  Create
    override fun create(postModel: PostModel) {
        return dao.create(postModel)
    }

    override fun update(postModel: PostModel) {
        return dao.update(postModel)
    }

    override fun delete(postModel: PostModel) {
        return dao.deletePost(postModel)
    }

    override fun search(query: CharSequence?, filter: Boolean): ArrayList<PostModel> {
        val searchedPosts = ArrayList<PostModel>()
        for(marks in posts){
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

    @SuppressLint("DefaultLocale")
    override fun searchCountries(query: CharSequence?): ArrayList<Country>{
        val searchedPosts = ArrayList<Country>()
        for(country in countries){
            if (country.countryName.toUpperCase().contains(query!!)){
                searchedPosts.add(country)
            }
        }
        return searchedPosts
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