package com.andor.navigate.logit.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andor.navigate.logit.R
import com.andor.navigate.logit.core.api.NetworkApi
import com.andor.navigate.logit.auth.implementation.NetworkApiImpl
import com.andor.navigate.logit.core.listener.DebouncedOnClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //can use dagger/koin for dependency injection here
    private lateinit var networkApi: NetworkApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkApi = NetworkApiImpl()

        /*
            setting on Click listener for the login button
            Listener used is With a debounce so even if the user click's login button multiple time's
            login will initiate only after debounce time
        */
        btn_login.setOnClickListener(object : DebouncedOnClickListener(1000) {
            override fun onDebouncedClick(v: View) {
                login(input_email.text.toString(), input_password.text.toString())
            }

        })
    }

    private fun login(email: String, password: String) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            networkApi.initiateLogin(email, password)
        } else {
            Toast.makeText(this, "Invalid Email and password", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Wiil validate the email here
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Wiil validate the password here
        return true
    }
}
