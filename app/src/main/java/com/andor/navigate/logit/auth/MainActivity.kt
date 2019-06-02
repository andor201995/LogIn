package com.andor.navigate.logit.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andor.navigate.logit.MyApplication
import com.andor.navigate.logit.R
import com.andor.navigate.logit.core.Utils
import com.andor.navigate.logit.core.api.ApiService
import com.andor.navigate.logit.core.listener.DebouncedOnClickListener
import com.andor.navigate.logit.core.model.Authorization
import com.andor.navigate.logit.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var session: Session
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.hide()
        if (application is MyApplication) {
            session = (application as MyApplication).getSession()
            apiService = (application as MyApplication).getApiService()

        }

        /*
            setting on Click listener for the login button
            Listener used is With a debounce so even if the user click's login button multiple time's
            login will initiate only after debounce time
        */
        btn_login.setOnClickListener(object : DebouncedOnClickListener(1000) {
            override fun onDebouncedClick(v: View) {
                if (!(application as MyApplication).getSession().isLoggedIn()) {
                    login(input_email.text.toString(), input_password.text.toString())
                } else {
                    startWelcomeActivity()
                }
            }

        })
    }

    private fun startWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        //add extra here is needed for the next activity
        startActivity(intent)
        finish()
    }

    private fun login(email: String, password: String) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            val loginAccount = apiService.loginAccount(Utils.getAuthorizationHeader(email, password))
            loginAccount.enqueue(object : Callback<Authorization> {
                override fun onFailure(call: Call<Authorization>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "login failed", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Authorization>, response: Response<Authorization>) {
                    progressBar.hide()
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "login successful", Toast.LENGTH_SHORT).show()
                        response.body()?.token?.let {
                            session.saveToken(it)
                            session.saveEmail(email)
                            session.savePassword(password)
                            startWelcomeActivity()
                            return
                        }
                        Toast.makeText(this@MainActivity, "login failed", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@MainActivity, "login failed", Toast.LENGTH_SHORT).show()
                    }
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
