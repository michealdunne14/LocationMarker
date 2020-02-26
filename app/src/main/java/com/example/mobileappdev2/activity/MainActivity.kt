package com.example.mobileappdev2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.TabsPagerAdapter
import com.example.mobileappdev2.adapter.LandmarkListener
import com.example.mobileappdev2.models.PostModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class MainActivity : AppCompatActivity(),AnkoLogger {
    lateinit var app : MainApp
    lateinit var pagerAdapter: TabsPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
////      Tabs pager adapter setup
//        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
//        info { "Main Activity Started" }
//        app = application as MainApp
////      Toolbar setup
//        toolbar.title = getString(R.string.locations_marker)
//        setSupportActionBar(toolbar)
//
////      Sets up view pager with pager adapter
//        mMainPager.adapter = pagerAdapter
//        mMainPager.currentItem = 1
////      Sets bottom navigation selected item to correct showing page
//        bottomNavigationView.selectedItemId = R.id.mNavHome
//
////      Sets up bottom navigation bar
//        mMainPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {}
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                when (position) {
//                    0 -> {
//                        bottomNavigationView.selectedItemId = R.id.mNavSettings
//                    }
//                    1 -> {
//                        bottomNavigationView.selectedItemId = R.id.mNavHome
//                    }
//                    else -> bottomNavigationView.selectedItemId = R.id.mNavHome
//                }
//            }
//        })
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> startActivityForResult<PostActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//
//
//    //  Navigating to the correct selected Item
//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.mNavSettings -> {
//                mMainPager.currentItem = 0
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.mNavHome -> {
//                mMainPager.currentItem = 1
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }
//
//    override fun onResume() {
//        super.onResume()
//        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
//    }
//
////  Start activity for result of selected post.
//    override fun onLandMarkClick(postModel: PostModel) {
//        info { "Landmark Clicked" }
//        startActivityForResult(intentFor<PostActivity>().putExtra("landmark_edit",postModel), 0)
//    }
}
