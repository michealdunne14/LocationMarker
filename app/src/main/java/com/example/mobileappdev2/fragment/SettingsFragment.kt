package com.example.mobileappdev2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.models.PostModel
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    lateinit var app : MainApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        app = activity!!.application as MainApp
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

//        view.mSettingsPosts.text = "Total Posts ${app.fireStore.findPosts().size}"

        val likedPosts = ArrayList<PostModel>()
        for (post in app.fireStore.findPosts()){
            if (post.postLiked){
                likedPosts.add(post)
            }
        }

        view.mSettingsLiked.text = "Liked Posts ${likedPosts.size}"
        return view
    }
}
