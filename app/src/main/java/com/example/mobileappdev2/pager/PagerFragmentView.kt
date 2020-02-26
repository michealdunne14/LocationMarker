package com.example.mobileappdev2.pager

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.PostActivity
import com.example.mobileappdev2.adapter.LandmarkListener
import com.example.mobileappdev2.adapter.TabsPagerAdapter
import com.example.mobileappdev2.models.PostModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.main_layout.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult


class PagerFragmentView : Fragment(),AnkoLogger,LandmarkListener {
    lateinit var app : MainApp
    lateinit var pagerAdapter: TabsPagerAdapter
    lateinit var pagerView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_pager, container, false)
        pagerView = view
        //      Tabs pager adapter setup
        pagerAdapter = TabsPagerAdapter(activity!!.supportFragmentManager)
        info { "Main Activity Started" }
        app = activity!!.application as MainApp

        pagerView.mMainToolbar.title = getString(R.string.locations_marker)
        (activity as AppCompatActivity?)!!.setSupportActionBar(pagerView.mMainToolbar)


        //      Sets up view pager with pager adapter
        pagerView.mMainPager.adapter = pagerAdapter
        pagerView.mMainPager.currentItem = 1
//      Sets bottom navigation selected item to correct showing page
        pagerView.bottomNavigationView.selectedItemId = R.id.mNavHome

        //      Sets up bottom navigation bar
        pagerView.mMainPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                        pagerView.bottomNavigationView.selectedItemId = R.id.mNavSettings
                    }
                    1 -> {
                        pagerView.bottomNavigationView.selectedItemId = R.id.mNavHome
                    }
                    else -> pagerView.bottomNavigationView.selectedItemId = R.id.mNavHome
                }
            }
        })
        pagerView.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        return view
    }

    //  Navigating to the correct selected Item
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.mNavSettings -> {
                pagerView.mMainPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.mNavHome -> {
                pagerView.mMainPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        pagerAdapter = TabsPagerAdapter(activity!!.supportFragmentManager)
    }

    //  Start activity for result of selected post.
    override fun onLandMarkClick(postModel: PostModel) {
        info { "Landmark Clicked" }
        startActivityForResult(activity!!.intentFor<PostActivity>().putExtra("landmark_edit",postModel), 0)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> activity!!.startActivityForResult<PostActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.main_menu,menu)
    }

}