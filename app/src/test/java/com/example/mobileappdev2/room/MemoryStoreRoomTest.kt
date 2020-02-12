package com.example.mobileappdev2.room

import com.example.mobileappdev2.models.PostModel
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class MemoryStoreRoomTest {

    private val postArrayList = ArrayList<PostModel>()
    private val postModel = PostModel()

    @Before
    fun setUp() {
        postModel.title = "Paris"
        postModel.country = "France"
        postModel.datevisted = "12/05/20"
        postModel.description = "I really enjoyed my time in france"
        postModel.id = 54028409234
        postArrayList.add(postModel)
    }

    @Test
    fun findAll() {
        assertEquals(postArrayList[0].title,"Paris")
    }

    @Test
    fun create() {

        postModel.title = "Lyon"
        postModel.country = "France"
        postModel.datevisted = "12/10/20"
        postModel.description = "I really enjoyed my time in france"
        postModel.id = 54028409234
        postArrayList.add(postModel)

        assertEquals(postArrayList[1].title,"Lyon")
    }

    @Test
    fun update() {
        postArrayList[0].title = "Lyon"
        var check = false
        if (postArrayList[0].title == "Paris") {
            check = true
        }
        assertFalse(postArrayList[0].title, check)
    }

    @Test
    fun delete() {
        postArrayList.clear()
        var check = false
        if(postArrayList.isEmpty()){
            check = true
        }
        assertTrue(check)
    }

}