package com.andor.navigate.logit.auth

import com.andor.navigate.logit.core.api.ApiService
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(
    private val apiService: ApiService,
    private val session: Session
) : Authenticator {
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
//        if (response.code() == 401) {
//            val refreshCall = refereshAccessToken(refereshToken)
//
//            //make it as retrofit synchronous call
//            val refreshResponse = refreshCall.execute()
//            return if (refreshResponse != null && refreshResponse!!.code() == 200) {
//                //read new JWT value from response body or interceptor depending upon your JWT availability logic
//                newCookieValue = readNewJwtValue()
//                response.request().newBuilder()
//                    .header("basic-auth", newCookieValue)
//                    .build()
//            } else {
//                null
//            }
//        }
        return null
    }
}