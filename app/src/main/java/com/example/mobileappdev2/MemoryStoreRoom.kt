package com.example.mobileappdev2

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.mobileappdev2.room.PostDao
import com.example.mobileappdev2.room.Database
import org.jetbrains.anko.AnkoLogger
import java.util.*
import kotlin.collections.ArrayList

class MemoryStoreRoom(val context: Context): PostStore, AnkoLogger {

    var dao: PostDao
    var countries = arrayListOf<Country>()
    var posts: List<PostModel> = ArrayList()


    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.postDao()
    }

    override fun findAll(): List<PostModel> {
        posts = dao.findAll()
        return posts
    }

    override fun findPosts(): List<PostModel>{
        return posts
    }

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
            val simpleCountry = Country(countryName,countryCode)
            countries.add(simpleCountry)
        }
        countries = ArrayList(countries.sortedWith(compareBy { it.countryName }))
    }

    override fun getCountryData(): ArrayList<Country> {
        return countries
    }
}