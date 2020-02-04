package com.example.mobileappdev2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(),LandmarkListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        val layoutManager = LinearLayoutManager(view.context)
        view.mLandmarkList.layoutManager = layoutManager as RecyclerView.LayoutManager?
        val arrayList: ArrayList<PostModel> = ArrayList()
        arrayList.add(PostModel("hello","test"))

        view.mLandmarkList.adapter = LandmarkAdapter(arrayList,this)

        return view
    }

    override fun onLandMarkClick(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
