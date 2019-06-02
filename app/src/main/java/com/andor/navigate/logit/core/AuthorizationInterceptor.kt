package com.andor.navigate.logit.core

import com.andor.navigate.logit.auth.Session
import com.andor.navigate.logit.core.api.ApiService
import okhttp3.Interceptor
import java.io.IOException

class AuthorizationInterceptor(private val apiService: ApiService, private val session: Session) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var mainResponse = chain.proceed(chain.request())
        val mainRequest = chain.request()

        if (session.isLoggedIn()) {
            // if response code is 401 or 403, 'mainRequest' has encountered authentication error
            if (mainResponse.code() == 401 || mainResponse.code() == 403) {
                val authKey = Utils.getAuthorizationHeader(session.getEmail(), session.getPassword())
                // request to login API to get fresh token
                // synchronously calling login API
                val loginResponse = apiService.loginAccount(authKey).execute()

                if (loginResponse.isSuccessful) {
                    // login request succeed, new token generated
                    val authorization = loginResponse.body()
                    // save the new token
                    session.saveToken(authorization!!.token!!)
                    // retry the 'mainRequest' which encountered an authentication error
                    // add new token into 'mainRequest' header and request again
                    val builder = mainRequest.newBuilder().header("Authorization", session.getToken())
                        .method(mainRequest.method(), mainRequest.body())
                    mainResponse = chain.proceed(builder.build())
                }
            }
        }

        return mainResponse
    }
}