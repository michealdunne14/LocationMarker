package com.example.mobileappdev2

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import kotlin.collections.ArrayList

class MemoryStore: PostStore,AnkoLogger {

    val landmarks = ArrayList<PostModel>()

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
    }

    override fun update(postModel: PostModel) {
        val post: PostModel? = landmarks.find { model -> model.id == postModel.id }
        if (post != null){
            post.title = postModel.title
            post.description = postModel.description
            post.images = postModel.images
            logAll()
        }
    }

    override fun delete(postModel: PostModel) {
        val post: PostModel? = landmarks.find { model -> model.id == postModel.id }
        if (post != null){
            landmarks.remove(post)
        }
    }

    fun logAll() {
        landmarks.forEach{ info("${it}") }
    }

}