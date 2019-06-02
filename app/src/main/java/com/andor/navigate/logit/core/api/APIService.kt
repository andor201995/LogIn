package com.andor.navigate.logit.core.api

import com.andor.navigate.logit.core.model.Authorization
import com.andor.navigate.logit.core.model.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    //used to get the auth-token
    @POST("login")
    fun loginAccount(@Header("Authorization") authKey: String): Call<Authorization>

    @GET("user")
    fun getUser(@Header("auth_token") token: String): Call<UserModel>


    companion object {
        // mockable url
        val URL = "https://demo6030150.mockable.io" + "/v1/"
    }
}
