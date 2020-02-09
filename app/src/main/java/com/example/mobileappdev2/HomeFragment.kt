package com.example.mobileappdev2

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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.mLandmarkList
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor

class HomeFragment : Fragment(),LandmarkListener,AnkoLogger {

    lateinit var app : MainApp
    lateinit var homeView: View
    var search = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        homeView = view
        app = activity!!.application as MainApp
        val layoutManager = LinearLayoutManager(view.context)
        view.mLandmarkList.layoutManager = layoutManager as RecyclerView.LayoutManager?
        doAsync {
            homeView.mLandmarkList.adapter = LandmarkAdapter(app.landmarks.findAll(), this@HomeFragment,app.landmarks)
        }

        app.landmarks.preparedata()

        homeView.mSearchFloatingActionButton.setOnClickListener {
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
                val searchedLandmarks = app.landmarks.search(characterSearch)
                homeView.mLandmarkList.adapter = LandmarkAdapter(
                    searchedLandmarks,
                    this@HomeFragment,
                    app.landmarks
                )
                homeView.mLandmarkList.adapter?.notifyDataSetChanged()
            }

        })
        return view
    }

    private fun showFilter(){
        search = true
        val constraintSet = ConstraintSet()
        constraintSet.clone(homeView.context, R.layout.fragment_home_search)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 500

        TransitionManager.beginDelayedTransition(fragment_home, transition)
        constraintSet.applyTo(fragment_home) //here constraint is the name of view to which we are applying the constraintSet
    }

    private fun cancelFilter(){
        search = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(homeView.context, R.layout.fragment_home)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 500

        TransitionManager.beginDelayedTransition(fragment_home, transition)
        constraintSet.applyTo(fragment_home)  //here constraint is the name of view to which we are applying the constraintSet
    }

    override fun onResume() {
        doAsync {
            homeView.mLandmarkList.adapter = LandmarkAdapter(
                app.landmarks.findAll(),
                this@HomeFragment,
                app.landmarks
            )
        }
        homeView.mLandmarkList.adapter?.notifyDataSetChanged()
        super.onResume()
    }

    override fun onLandMarkClick(postModel: PostModel) {
        startActivityForResult(context?.intentFor<PostActivity>()!!.putExtra("landmark_edit",postModel), 0)
    }

}
