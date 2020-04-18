package com.example.mobileappdev2.fragment.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.LandmarkAdapter
import com.example.mobileappdev2.adapter.LandmarkListener
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.animation.getNavOptions
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.pager.PagerFragmentViewDirections
import kotlinx.android.synthetic.main.fragment_favourite.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class FavouriteFragmentView : BaseView(), AnkoLogger, LandmarkListener {
    lateinit var presenter: FavouritePresenter
    lateinit var favouriteView: View

    companion object {
        fun newInstance() = FavouriteFragmentView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_favourite, container, false)

        info { "Favorite Fragment started" }
        presenter = initPresenter(FavouritePresenter(this)) as FavouritePresenter
        favouriteView = view
        val layoutManager = LinearLayoutManager(view.context)

        view.mFavouriteList.layoutManager = layoutManager as RecyclerView.LayoutManager?

        view.mFavouriteList.adapter = LandmarkAdapter(presenter.doGetFavourites(),this,presenter)
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            favouriteView.mFavouriteList.adapter = LandmarkAdapter(presenter.doGetFavourites(),this,presenter)
        }
    }

    override fun onLandMarkClick(postModel: PostModel) {
        info{ "Landmark Clicked"}
        val action = PagerFragmentViewDirections.actionPagerFragmentViewToPostFragment(postModel)
        favouriteView.findNavController().navigate(action,
            getNavOptions()
        )
    }

}
