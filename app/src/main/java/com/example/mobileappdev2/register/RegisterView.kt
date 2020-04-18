package com.example.mobileappdev2.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.MainActivity
import com.example.mobileappdev2.base.BaseView
import kotlinx.android.synthetic.main.fragment_register.view.*
import org.jetbrains.anko.AnkoLogger

class RegisterView : BaseView(),AnkoLogger {

    lateinit var presenter: RegisterPresenter
    lateinit var registerView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)
        registerView = view
        presenter = initPresenter(RegisterPresenter(this)) as RegisterPresenter

        view.mRegisterSignupButton.setOnClickListener {
            val name = view.mRegisterFirstname.text.toString() + " " + view.mRegisterSurname.text.toString()
            presenter.doRegister(view.mRegisterEmail.text.toString(),view.mRegisterPassword.text.toString(),name)
        }


        view.mRegisterReturnButton.setOnClickListener {
            view.findNavController().navigateUp()
        }

        return view
    }

    override fun gotoMainPageFromRegister(){
        this.startActivity(Intent(registerView.context, MainActivity::class.java))
        this.activity!!.finish()
    }
}
