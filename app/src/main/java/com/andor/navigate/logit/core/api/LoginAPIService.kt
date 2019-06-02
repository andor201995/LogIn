package com.andor.navigate.logit.core.api

import com.andor.navigate.logit.auth.Authorization
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginAPIService {
    //used to get the auth-token
    @POST("login")
    fun loginAccount(@Header("Authorization") authKey: String): Call<Authorization>
}
