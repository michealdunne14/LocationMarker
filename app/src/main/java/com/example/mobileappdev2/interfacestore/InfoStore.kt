package com.example.mobileappdev2.interfacestore

import android.view.View
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.models.Country
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel

interface InfoStore {
    fun findAll(): List<PostModel>
    fun searchCountries(query: CharSequence?): ArrayList<Country>
    fun create(postModel: PostModel, view: View)
    fun update(postModel: PostModel,view: View)
    fun delete(postModel: PostModel)
    fun search(query: CharSequence?, filter: Boolean, view: BaseView)
    fun preparedata()
    fun getCountryData(): ArrayList<Country>
    fun createUsers(userModel: UserModel)
    fun updateFavourite(postModel: PostModel)
    fun updateLike(postModel: PostModel)
    fun currentUser(): UserModel
    fun findFavourites(): ArrayList<PostModel>
    fun findSearchedPosts():ArrayList<PostModel>
}