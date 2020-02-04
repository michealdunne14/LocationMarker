package com.example.mobileappdev2

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return SettingsFragment()
            }
            1 -> {
                return HomeFragment()
            }
            else -> return HomeFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }


    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Settings"
            1 -> return "Home"
            else -> return null
        }
    }

}