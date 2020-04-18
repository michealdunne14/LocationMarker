package com.example.mobileappdev2.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobileappdev2.R
import com.example.mobileappdev2.activity.MainActivity
import com.example.mobileappdev2.animation.getNavOptions
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.login.LoginActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_start.view.*

class StartView : BaseView(),View.OnClickListener {

    companion object {
        private const val RC_SIGN_IN = 123
    }

    lateinit var presenter: StartPresenter
    lateinit var startView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        startView = view
        presenter = initPresenter(StartPresenter(this)) as StartPresenter

//
//        view.mStartLogin.setOnClickListener {
//            // Choose authentication providers
//            val providers = arrayListOf(
//                AuthUI.IdpConfig.EmailBuilder().build())
//
//// Create and launch sign-in intent
//            startActivityForResult(
//                AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setAvailableProviders(providers)
//                    .build(),
//                RC_SIGN_IN)
////            val action = StartFragmentDirections.actionStartFragmentToLoginFragment()
////            view.findNavController().navigate(action, getNavOptions())
//        }
//
//        view.mStartRegister.setOnClickListener {
//            val action = StartViewDirections.actionStartFragmentToRegisterFragment()
//            view.findNavController().navigate(action, getNavOptions())
//        }

        createSignInIntent()

        return view
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        val customLayout = AuthMethodPickerLayout
            .Builder(R.layout.fragment_start)
            .setGoogleButtonId(R.id.googleSignInButton)
            .setEmailButtonId(R.id.emailSignInButton)
            .setPhoneButtonId(R.id.phoneSignInButton)
            .build()

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false,true) // true,true for Smart Lock
                //.setLogo(R.drawable.login_homer)
                .setTheme(R.style.FirebaseLoginTheme)
                .setAuthMethodPickerLayout(customLayout)
                .build(),
            123)
        // [END auth_fui_create_intent]

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
//                presenter.setData()
                presenter.fetchPosts()
            } else {
                startActivity(Intent(startView.context, LoginActivity::class.java))
            }
        }
    }

    override fun gotoMainPage(){
        if (activity != null) {
            startActivity(Intent(startView.context, MainActivity::class.java))
            activity!!.finish()
        }
    }

    override fun onClick(v: View) { createSignInIntent() }
}
