package com.example.mobileappdev2

import androidx.navigation.NavOptions

fun getNavOptions(): NavOptions? {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_left)
        .build()
}