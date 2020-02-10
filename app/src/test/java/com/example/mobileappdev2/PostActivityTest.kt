package com.example.mobileappdev2

import android.R
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.widget.EditText
import com.example.mobileappdev2.activity.PostActivity
import kotlinx.android.synthetic.main.activity_post.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

lateinit var postActivity: PostActivity

@RunWith(MockitoJUnitRunner::class)
class PostActivityTest {


    @Before
    fun setUp() {
        postActivity = PostActivity()

    }

    @Test
    fun onCreate() {
        postActivity.mPostTitle.setText("Hello")
        assertEquals("Hello", postActivity.mPostTitle)
    }
}