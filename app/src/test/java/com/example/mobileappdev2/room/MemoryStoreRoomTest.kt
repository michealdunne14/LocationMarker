package com.example.mobileappdev2.room

import android.content.Context
import com.example.mobileappdev2.models.PostModel
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MemoryStoreRoomTest {

    @Mock
    lateinit var context: Context

    @Before
    fun setUp() {
    }

    @Test
    fun findAll() {
    }

    @Test
    fun create() {
        val postModel = PostModel()
        val memoryStoreRoom = MemoryStoreRoom(context)
        postModel.title = "Paris"
        postModel.country = "France"
        postModel.datevisted = "12/05/20"
        postModel.description = "I really enjoyed my time in france"
        postModel.id = 54028409234
        memoryStoreRoom.create(postModel)
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun searchCountries() {
    }
}