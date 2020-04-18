package com.example.mobileappdev2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mobileappdev2.fragment.favourite.FavouriteFragmentView
import com.example.mobileappdev2.fragment.home.HomeView
import com.example.mobileappdev2.fragment.settings.SettingsView

class TabsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return SettingsView.newInstance()
            }
            1 -> {
                return HomeView.newInstance()
            }
            2 -> {
                return FavouriteFragmentView.newInstance()
            }
            else -> return HomeView()
        }
    }

    override fun getCount(): Int {
        return 3
    }


    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Settings"
            1 -> return "Home"
            2 -> return "Favourite"
            else -> return null
        }
    }

}