package com.example.mobileappdev2

import android.content.Context
import androidx.room.Room
import com.example.mobileappdev2.room.PostDao
import com.example.mobileappdev2.room.Database
import org.jetbrains.anko.AnkoLogger
import java.util.*
import kotlin.collections.ArrayList

class MemoryStoreRoom(val context: Context): PostStore, AnkoLogger {

    var dao: PostDao
    var normalArray = arrayListOf<Country>()
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

    override fun create(postModel: PostModel) {
        return dao.create(postModel)
    }

    override fun update(postModel: PostModel) {
        return dao.update(postModel)
    }

    override fun delete(postModel: PostModel) {
        return dao.deletePost(postModel)
    }

    override fun search(query: CharSequence?): ArrayList<PostModel> {
        val searchedPosts = ArrayList<PostModel>()
        for(marks in posts){
            if (marks.title.contains(query!!)){
                searchedPosts.add(marks)
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
            normalArray.add(simpleCountry)
        }
        normalArray = ArrayList(normalArray.sortedWith(compareBy { it.countryName }))
    }

    override fun getCountryData(): ArrayList<Country> {
        return normalArray
    }
}