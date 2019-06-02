package com.andor.navigate.logit.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andor.navigate.logit.R
import com.andor.navigate.logit.core.AuthHelper
import com.andor.navigate.logit.core.DebouncedOnClickListener
import com.andor.navigate.logit.core.NetworkCallback
import com.andor.navigate.logit.core.NetworkRequest
import com.andor.navigate.logit.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_main.*

class AuthActivity : AppCompatActivity() {

    private lateinit var networkRequest: NetworkRequest
    private lateinit var authHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.hide()
        authHelper = AuthHelper.getInstance(this)
        networkRequest = NetworkRequest(this)
        /*
            setting on Click listener for the login button
            Listener used is With a debounce so even if the user click's login button multiple time's
            login will initiate only after debounce time
        */
        btn_login.setOnClickListener(object : DebouncedOnClickListener(1000) {
            override fun onDebouncedClick(v: View) {
                if (!authHelper.isLoggedIn) {
                    login(input_email.text.toString(), input_password.text.toString())
                } else {
                    startWelcomeActivity()
                }
            }

        })
    }

    private fun startWelcomeActivity() {
        //add extra here is needed for the next activity
        startActivity(WelcomeActivity.getIntent(this))
        finish()
    }

    private fun login(email: String, password: String) {
        if (isEmailValid(email) && isPasswordValid(password)) {

            networkRequest.postLoginRequest(email, password, object : NetworkCallback<Authorization> {
                override fun onResponse(response: Authorization) {
                    Toast.makeText(this@AuthActivity, "login successful", Toast.LENGTH_SHORT).show()
                    progressBar.hide()
                    authHelper.setIdToken(response)
                    startWelcomeActivity()
                }

                override fun onError(error: String) {
                    Toast.makeText(this@AuthActivity, "login failed", Toast.LENGTH_SHORT).show()
                }

                override fun type(): Class<Authorization> {
                    return Authorization::class.java
                }

            })
        } else {
            Toast.makeText(this, "Invalid Email and password", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Will validate the email here
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Will validate the password here
        return true
    }

}
