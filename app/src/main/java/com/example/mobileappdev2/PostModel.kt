package com.example.mobileappdev2

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mobileappdev2.room.Converters
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class PostModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                     var title: String = "",
                     var description: String = "",
                     var country: String = "",
                     var datevisted: String = "",
                     var images: List<String> = ArrayList()
): Parcelable