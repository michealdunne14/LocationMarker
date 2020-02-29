package com.example.mobileappdev2.firebase

import android.content.Context
import com.example.mobileappdev2.interfacestore.PostStore
import com.example.mobileappdev2.models.Country
import com.example.mobileappdev2.models.PostModel
import org.jetbrains.anko.AnkoLogger

class FireStore(context: Context): PostStore, AnkoLogger {

    val posts = ArrayList<PostModel>()

    override fun findAll(): List<PostModel> {
        return posts
    }

    override fun findPosts(): List<PostModel> {
        return posts
    }

    override fun searchCountries(query: CharSequence?): ArrayList<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun search(query: CharSequence?, filter: Boolean): ArrayList<PostModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun preparedata() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountryData(): ArrayList<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}