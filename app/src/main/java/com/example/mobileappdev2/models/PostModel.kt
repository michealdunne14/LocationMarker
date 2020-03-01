package com.example.mobileappdev2.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class PostModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                     var title: String = "",
                     var fbId: String = "",
                     var description: String = "",
                     var country: String = "",
                     var datevisted: String = "",
                     var postLiked: Boolean = false,
                     var favourite: Boolean = false,
                     var images: ArrayList<String> = ArrayList()
): Parcelable