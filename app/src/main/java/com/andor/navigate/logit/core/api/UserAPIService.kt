package com.andor.navigate.logit.core.api

import com.andor.navigate.logit.welcome.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UserAPIService {

    @GET(value = "user")
    fun getUser(@Header(value = "auth_token") token: String): Call<UserModel>

}