package com.example.mobileappdev2.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_login, container, false)


        view.mLoginSignInButton.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
            activity!!.finish()
        }

        view.mRegisterReturnButton.setOnClickListener {
            view.findNavController().navigateUp()
        }
        return view
    }

}
