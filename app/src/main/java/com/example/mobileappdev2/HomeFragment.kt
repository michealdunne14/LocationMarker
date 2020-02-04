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

    lateinit var app : MainApp
    lateinit var homeView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        homeView = view
        app = activity!!.application as MainApp
        val layoutManager = LinearLayoutManager(view.context)
        view.mLandmarkList.layoutManager = layoutManager as RecyclerView.LayoutManager?
        homeView.mLandmarkList.adapter = LandmarkAdapter(app.landmarks.findAll(),this)
        return view
    }

    override fun onResume() {
        homeView.mLandmarkList.adapter?.notifyDataSetChanged()
        super.onResume()
    }

    override fun onLandMarkClick(postModel: PostModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
