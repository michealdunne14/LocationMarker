package com.example.mobileappdev2.base

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.MainActivity
import com.example.mobileappdev2.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
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
}