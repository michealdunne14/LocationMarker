package com.example.mobileappdev2.fragment.settings

import android.content.Intent
import android.view.View
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.example.mobileappdev2.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import org.jetbrains.anko.AnkoLogger

class SettingsPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var fireStore: FireStore

    init {
        fireStore = app.fireStore as FireStore
    }

    fun doLogout(view: View) {
        AuthUI.getInstance()
            .signOut(view.context)
            .addOnCompleteListener {
                fireStore.clearData()
                this.view.startActivity(Intent(this.view.context, LoginActivity::class.java))
            }
    }
}