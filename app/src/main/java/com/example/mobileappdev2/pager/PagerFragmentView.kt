package com.example.mobileappdev2.pager

import android.app.ProgressDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.LandmarkListener
import com.example.mobileappdev2.adapter.TabsPagerAdapter
import com.example.mobileappdev2.models.PostModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_layout.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


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
        pagerAdapter = TabsPagerAdapter(childFragmentManager)
        info { "Main Activity Started" }
        app = activity!!.application as MainApp

        pagerView.mMainToolbar.title = getString(R.string.locations_marker)
        (activity as AppCompatActivity?)!!.setSupportActionBar(pagerView.mMainToolbar)
        setHasOptionsMenu(true)


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
                    2 -> {
                        pagerView.bottomNavigationView.selectedItemId = R.id.mNavFavourite
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
            R.id.mNavFavourite -> {
                pagerView.mMainPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //  Start activity for result of selected post.
    override fun onLandMarkClick(postModel: PostModel) {
        info { "Landmark Clicked" }
        val action = PagerFragmentViewDirections.actionPagerFragmentViewToPostFragment(postModel)
        pagerView.findNavController().navigate(action)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val action = PagerFragmentViewDirections.actionPagerFragmentViewToPostFragment(PostModel())
                pagerView.findNavController().navigate(action)
            }
            R.id.item_map -> {
                val action = PagerFragmentViewDirections.actionPagerFragmentViewToMapFragment()
                pagerView.findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

}