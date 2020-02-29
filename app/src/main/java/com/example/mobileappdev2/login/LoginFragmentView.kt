package com.example.mobileappdev2.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.MainActivity
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.firebase.FireStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class LoginFragmentView : BaseView(),AnkoLogger {


    lateinit var presenter: LoginFragmentPresenter
    lateinit var loginView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_login, container, false)
        loginView = view
        presenter = initPresenter(LoginFragmentPresenter(this)) as LoginFragmentPresenter

        view.mLoginSignInButton.setOnClickListener {
            val email = view.mLoginEmail.text.toString()
            val password = view.mLoginPassword.text.toString()
            presenter.doLogin(email,password)
        }

        view.mRegisterReturnButton.setOnClickListener {
            view.findNavController().navigateUp()
        }
        return view
    }

    override fun gotoMainPage(){
        this.startActivity(Intent(loginView.context, MainActivity::class.java))
        this.activity!!.finish()
    }

}
