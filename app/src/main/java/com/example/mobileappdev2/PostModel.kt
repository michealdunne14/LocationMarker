package com.example.mobileappdev2

data class PostModel(
    var title: String = "",
    var description: String = "",
    var address: String = "",
    var images: ArrayList<String> = ArrayList()
)