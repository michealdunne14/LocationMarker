package com.example.mobileappdev2.interfacestore

import com.example.mobileappdev2.models.Country
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.models.UserModel

interface InfoStore {
    fun findAll(): List<PostModel>
    fun findPosts(): List<PostModel>
    fun searchCountries(query: CharSequence?): ArrayList<Country>
    fun create(postModel: PostModel)
    fun update(postModel: PostModel)
    fun delete(postModel: PostModel)
    fun search(query: CharSequence?, filter: Boolean): ArrayList<PostModel>
    fun preparedata()
    fun getCountryData(): ArrayList<Country>
    fun createUsers(userModel: UserModel)
}