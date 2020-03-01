package com.example.mobileappdev2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mobileappdev2.favourite.FavouriteFragment
import com.example.mobileappdev2.fragment.HomeFragment
import com.example.mobileappdev2.fragment.SettingsFragment

class TabsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return SettingsFragment.newInstance()
            }
            1 -> {
                return HomeFragment.newInstance()
            }
            2 -> {
                return FavouriteFragment.newInstance()
            }
            else -> return HomeFragment()
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