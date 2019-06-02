package com.andor.navigate.logit.welcome

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.andor.navigate.logit.MyApplication
import com.andor.navigate.logit.R
import com.andor.navigate.logit.core.model.UserModel
import kotlinx.android.synthetic.main.activity_welcome.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    override fun onStart() {
        super.onStart()
        (application as MyApplication).getApiService().getUser((application as MyApplication).getSession().getToken())
            .enqueue(object : Callback<UserModel> {
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    welcomeTxt.setText("Error", TextView.BufferType.EDITABLE)
                }

                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    welcomeTxt.setText(response.body()?.name, TextView.BufferType.EDITABLE)
                }

            })
    }
}
