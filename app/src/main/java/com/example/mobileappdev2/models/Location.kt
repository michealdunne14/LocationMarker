package com.example.mobileappdev2.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Location (
    var title: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
): Parcelable