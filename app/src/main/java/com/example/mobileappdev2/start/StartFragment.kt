package com.example.mobileappdev2.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobileappdev2.R
import com.example.mobileappdev2.getNavOptions
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.fragment_start.view.*

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start, container, false)


        view.mStartLogin.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToLoginFragment()
            view.findNavController().navigate(action, getNavOptions())
        }

        view.mStartRegister.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToRegisterFragment()
            view.findNavController().navigate(action,getNavOptions())
        }

        return view
    }
}
