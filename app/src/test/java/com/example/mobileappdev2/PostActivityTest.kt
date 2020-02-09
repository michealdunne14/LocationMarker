package com.example.mobileappdev2

import android.R
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_post.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class PostActivityTest {

    lateinit var activity: Activity
    lateinit var title: EditText

    @Before
    fun setUp() {

//        title = activity.findViewById(R.id.mPostTitle) as EditText
//        R.attr.password = mActivity.findViewById(com.wallproductions.gas.tracker.R.id.login_password)

    }

    @Test
    fun onCreate() {
    }
}