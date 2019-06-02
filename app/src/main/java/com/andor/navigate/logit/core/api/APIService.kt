package com.andor.navigate.logit.core.api

import com.andor.navigate.logit.core.model.Authorization
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun loginAccount(@Header("Authorization") authKey: String): Call<Authorization>


    companion object {
        // dummy url
        val URL = "https://api.example.com" + "/v1/"
    }
}
