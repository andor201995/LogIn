package com.andor.navigate.logit.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.andor.navigate.logit.R
import com.andor.navigate.logit.core.AuthHelper
import com.andor.navigate.logit.core.NetworkCallback
import com.andor.navigate.logit.core.NetworkRequest
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var networkRequest: NetworkRequest
    private lateinit var authHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        this.authHelper = AuthHelper.getInstance(this)
        this.networkRequest = NetworkRequest(this)

        if (authHelper.isLoggedIn) {
            setUpView()
        } else {
            finish()
        }
    }

    private fun setUpView() {
        networkRequest.getUserdetail(object : NetworkCallback<UserModel> {
            override fun onResponse(response: UserModel) {
                welcomeTxt.setText(response.name, TextView.BufferType.EDITABLE)
            }

            override fun onError(error: String) {
                welcomeTxt.setText("Error", TextView.BufferType.EDITABLE)
            }

            override fun type(): Class<UserModel> {
                return UserModel::class.java
            }

        })
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, WelcomeActivity::class.java)
        }
    }
}
