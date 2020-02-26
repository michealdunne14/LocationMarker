package com.example.mobileappdev2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobileappdev2.activity.MainActivity
import com.example.mobileappdev2.activity.PostActivity
import com.example.mobileappdev2.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_register.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult

class RegisterFragment : Fragment(),AnkoLogger {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        view.mRegisterSignupButton.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
            activity!!.finish()
        }


        view.mRegisterReturnButton.setOnClickListener {
            view.findNavController().navigateUp()
        }

        return view
    }
}
