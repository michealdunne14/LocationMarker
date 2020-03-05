package com.example.mobileappdev2.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Location (
    val title: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
): Parcelable