package com.example.mobileappdev2.base

import android.app.ProgressDialog
import androidx.fragment.app.Fragment
import com.example.mobileappdev2.models.Location
import com.example.mobileappdev2.models.PostModel
import org.jetbrains.anko.AnkoLogger

enum class VIEW {
    MAIN,START
}

open abstract class BaseView: Fragment(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    open fun doSetDetails(email: String,password: String,name: String) {}
    open fun showFloatingAction(){}
    open fun hideFloatingAction(){}
    open fun gotoMainPage(){}
    open fun gotoMainPageFromRegister(){}
    open fun setImages(imageArrayList: ArrayList<String>) {}
    open fun searchLandmarks(findSearchedPosts: ArrayList<PostModel>) {}
    open fun notifyDataChange(){}
    open fun setLocationName(landmarkName: String) {}
    open fun navigateUp(){}
    open fun hideCustomButton(){}
    open fun dialogDismiss(){}
    open fun markerLocations(
        location: Location,
        post: PostModel,
        index: Int
    ) {}
}