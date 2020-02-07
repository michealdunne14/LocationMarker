package com.example.mobileappdev2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class MainActivity : AppCompatActivity(),LandmarkListener {
    lateinit var app : MainApp
    lateinit var pagerAdapter: TabsPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
        app = application as MainApp

        toolbar.title = title
        setSupportActionBar(toolbar)


        mMainPager.adapter = pagerAdapter
        mMainPager.currentItem = 1
        bottomNavigationView.selectedItemId = R.id.mNavHome
        mMainPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigationView.selectedItemId = R.id.mNavSettings
                    }
                    1 -> {

                        bottomNavigationView.selectedItemId = R.id.mNavHome
                    }
                    else -> bottomNavigationView.selectedItemId = R.id.mNavHome
                }
            }
        })
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> startActivityForResult<PostActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }



    //  Navigating to the correct selected Item
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.mNavSettings -> {
                mMainPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.mNavHome -> {
                mMainPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onLandMarkClick(postModel: PostModel) {
        startActivityForResult(intentFor<PostActivity>().putExtra("landmark_edit",postModel), 0)
    }
}
