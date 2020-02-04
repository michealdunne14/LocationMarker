package com.example.mobileappdev2

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var address: String = "",
    var images: ArrayList<String> = ArrayList()
): Parcelable