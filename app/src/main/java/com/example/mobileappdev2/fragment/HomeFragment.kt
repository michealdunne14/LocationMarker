package com.example.mobileappdev2.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.activity.PostActivity
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.LandmarkAdapter
import com.example.mobileappdev2.adapter.LandmarkListener
import com.example.mobileappdev2.models.PostModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.mLandmarkList
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor

class HomeFragment : Fragment(),
    LandmarkListener,AnkoLogger {

    lateinit var app : MainApp
    lateinit var homeView: View
    var search = false
    var filter = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        homeView = view
        info { "Home Fragment Started" }
        app = activity!!.application as MainApp
        val layoutManager = LinearLayoutManager(view.context)
        view.mLandmarkList.layoutManager = layoutManager as RecyclerView.LayoutManager?
//      Get all Landmarks and add it to an adapter
        doAsync {
            homeView.mLandmarkList.adapter = LandmarkAdapter(app.landmarks.findAll(), this@HomeFragment,app.landmarks)
        }

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

//      Get all countries and add it to an arraylist
        app.landmarks.preparedata()

        homeView.mSearchFloatingActionButton.setOnClickListener {
            info { "Floating action Button" }
            if (!search){
                showFilter()
            }else{
                cancelFilter()
                homeView.mLandmarkList.adapter =
                    LandmarkAdapter(
                        app.landmarks.findPosts(),
                        this@HomeFragment,
                        app.landmarks
                    )
                homeView.mLandmarkList.adapter?.notifyDataSetChanged()
            }
        }


        homeView.mHomeSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(characterSearch: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchedLandmarks = app.landmarks.search(characterSearch,filter)
                homeView.mLandmarkList.adapter =
                    LandmarkAdapter(
                        searchedLandmarks,
                        this@HomeFragment,
                        app.landmarks
                    )
                homeView.mLandmarkList.adapter?.notifyDataSetChanged()
            }

        })
        return view
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
    }
// When
    override fun onResume() {
        doAsync {
            homeView.mLandmarkList.adapter =
                LandmarkAdapter(
                    app.landmarks.findAll(),
                    this@HomeFragment,
                    app.landmarks
                )
        }
        homeView.mLandmarkList.adapter?.notifyDataSetChanged()
        super.onResume()
    }

    override fun onLandMarkClick(postModel: PostModel) {
        info{ "Landmark Clicked"}
        startActivityForResult(context?.intentFor<PostActivity>()!!.putExtra("landmark_edit",postModel), 0)
    }

}
