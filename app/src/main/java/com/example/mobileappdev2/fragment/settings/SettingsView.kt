package com.example.mobileappdev2.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.models.PostModel
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsView : BaseView() {

    lateinit var app : MainApp
    lateinit var presenter: SettingsPresenter

    companion object {
        fun newInstance() =
            SettingsView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        app = activity!!.application as MainApp
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        view.mLogout.setOnClickListener {
            presenter.doLogout(view)
        }

        view.mSettingsLiked.text = "Liked Posts ${presenter.fireStore.totalLikes}"
        view.mSettingsPosts.text = "Posts ${presenter.fireStore.totalPosts}"
        view.mSettingsFavourites.text = "Favourite Posts ${presenter.fireStore.totalFavourites}"
        view.mSettingsUsers.text = "Users ${presenter.fireStore.totalUsers}"
        return view
    }
}
