package com.example.mobileappdev2

interface PostStore {
    fun findAll(): List<PostModel>
    fun create(postModel: PostModel)
    fun update(postModel: PostModel)
    fun delete(postModel: PostModel)
}