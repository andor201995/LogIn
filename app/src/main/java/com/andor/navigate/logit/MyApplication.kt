package com.andor.navigate.logit

import android.app.Application
import com.andor.navigate.logit.auth.Session
import com.andor.navigate.logit.core.AuthorizationInterceptor
import com.andor.navigate.logit.core.Utils.Companion.getAuthorizationHeader
import com.andor.navigate.logit.core.api.ApiService
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class MyApplication : Application() {

    private lateinit var session: Session
    private lateinit var apiService: ApiService
    private var authenticationListener: AuthenticationListener? = null

    // use a storage option to store the
    // credentails and user info
    // i.e: SQLite, SharedPreference etc.
    fun getSession(): Session {
        if (!::session.isInitialized) {
            session = object : Session {
                override fun isLoggedIn(): Boolean {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun getToken(): String {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun getEmail(): String {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun getPassword(): String {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun saveToken(token: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun saveEmail(email: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun savePassword(password: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun invalidate() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
        }

        return session
    }

    interface AuthenticationListener {
        fun onUserLoggedOut()
    }

    fun setAuthenticationListener(listener: AuthenticationListener) {
        this.authenticationListener = listener
    }

    fun getApiService(): ApiService {
        if (!::apiService.isInitialized) {
            apiService = provideRetrofit(ApiService.URL).create<ApiService>(ApiService::class.java)
        }
        return apiService
    }

    private fun provideRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.addInterceptor(AuthorizationInterceptor(getApiService(), getSession()))

        return okhttpClientBuilder.build()
    }
}