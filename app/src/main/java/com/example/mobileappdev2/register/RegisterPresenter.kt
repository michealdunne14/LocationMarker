package com.example.mobileappdev2.register

import com.example.mobileappdev2.MainApp
import com.example.mobileappdev2.models.UserModel
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.base.BaseView
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class RegisterPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
    override var app : MainApp = view.activity?.application as MainApp
    var user = UserModel()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doRegister(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.name = name
                user.email = email
                user.password = password
                app.fireStore.createUsers(user)
                view.gotoMainPageFromRegister()
            } else {
                view.activity?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }
}