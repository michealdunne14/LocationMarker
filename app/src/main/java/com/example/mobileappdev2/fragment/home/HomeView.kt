package com.example.mobileappdev2.fragment.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.LandmarkAdapter
import com.example.mobileappdev2.adapter.LandmarkListener
import com.example.mobileappdev2.animation.getNavOptions
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.pager.PagerFragmentViewDirections
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.mLandmarkList
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HomeView : BaseView(), LandmarkListener,AnkoLogger {

    lateinit var app : MainApp
    lateinit var homeView: View
    var search = false
    var filter = false
    lateinit var presenter: HomePresenter

    companion object {
        fun newInstance() = HomeView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        presenter = initPresenter(HomePresenter(this)) as HomePresenter

        homeView = view
        info { "Home Fragment Started" }
        app = activity!!.application as MainApp
        val layoutManager = LinearLayoutManager(view.context)

        view.mLandmarkList.layoutManager = layoutManager as RecyclerView.LayoutManager?
//      Get all Landmarks and add it to an adapter

//      Filtering items by either likes or all
        homeView.mFilteringItems.setOnClickListener {
            if (filter){
                filter = false
                homeView.mFilteringItems.text = getString(R.string.filter_all)
            }else{
                filter = true
                homeView.mFilteringItems.text = getString(R.string.filter_by_likes)
            }
        }

        presenter.doPrepareData()

        homeView.mSearchFloatingActionButton.setOnClickListener {
            info { "Floating action Button" }
            if (!search){
                showFilter()
            }else{
                cancelFilter()
            }
        }


        homeView.mHomeSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(characterSearch: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.homeSearch(characterSearch,filter)
            }

        })
        return view
    }

    override fun onResume() {
        super.onResume()
        searchLandmarks(presenter.findAll())
    }

    override fun searchLandmarks(findSearchedPosts: ArrayList<PostModel>) {
        homeView.mLandmarkList.adapter = LandmarkAdapter(findSearchedPosts, this@HomeView, presenter)
        homeView.mLandmarkList.adapter?.notifyDataSetChanged()
    }

//  Show Search Filter
    private fun showFilter(){
        search = true
        val constraintSet = ConstraintSet()
        constraintSet.clone(homeView.context,
            R.layout.fragment_home_search
        )

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 500

        TransitionManager.beginDelayedTransition(fragment_home, transition)
        constraintSet.applyTo(fragment_home) //here constraint is the name of view to which we are applying the constraintSet
    }
    //  Cancel Search Filter
    private fun cancelFilter(){
        search = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(homeView.context,
            R.layout.fragment_home
        )

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 500

        TransitionManager.beginDelayedTransition(fragment_home, transition)
        constraintSet.applyTo(fragment_home)  //here constraint is the name of view to which we are applying the constraintSet
        searchLandmarks(presenter.findAll())
    }


    override fun onLandMarkClick(postModel: PostModel) {
        info{ "Landmark Clicked"}
        val action = PagerFragmentViewDirections.actionPagerFragmentViewToPostFragment(postModel)
        homeView.findNavController().navigate(action,
            getNavOptions()
        )
    }

}
