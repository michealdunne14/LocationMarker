package com.example.mobileappdev2

interface PostStore {
    fun findAll(): List<PostModel>
    fun findPosts(): List<PostModel>
    fun create(postModel: PostModel)
    fun update(postModel: PostModel)
    fun delete(postModel: PostModel)
    fun search(query: CharSequence?, filter: Boolean): ArrayList<PostModel>
    fun preparedata()
    fun getCountryData(): ArrayList<Country>
}