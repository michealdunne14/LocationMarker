package com.example.mobileappdev2

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MemoryStore: PostStore,AnkoLogger {

    val landmarks = ArrayList<PostModel>()

    override fun findAll(): ArrayList<PostModel> {
        return landmarks
    }

    override fun create(postModel: PostModel) {
        landmarks.add(postModel)
        logAll()
    }

    override fun update(postModel: PostModel) {

    }

    override fun delete(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun logAll() {
        landmarks.forEach{ info("${it}") }
    }

}