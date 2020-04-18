package com.example.mobileappdev2.animation

import androidx.navigation.NavOptions
import com.example.mobileappdev2.R

fun getNavOptions(): NavOptions? {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_right)
        .setPopEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
}