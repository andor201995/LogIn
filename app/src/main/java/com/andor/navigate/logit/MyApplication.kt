package com.andor.navigate.logit

import android.app.Application
import android.content.SharedPreferences
import com.andor.navigate.logit.auth.Session
import com.andor.navigate.logit.core.Utils
import com.andor.navigate.logit.core.api.ApiService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MyApplication : Application() {

    private lateinit var mPreferences: SharedPreferences
    private lateinit var session: Session
    private lateinit var apiService: ApiService
    private lateinit var authenticationListener: AuthenticationListener

    companion object {
        const val MY_PREFS_NAME = "com.andor.navigate.authsharedprefs"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val TOKEN = "token"
        const val DEFAULT = "default"
    }

    override fun onCreate() {
        super.onCreate()
        mPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
        //initiating on create
        getApiService()
        getSession()

    }

    // use a storage option to store the
    // credentails and user info
    // i.e: SQLite, SharedPreference etc.
    fun getSession(): Session {
        if (!::session.isInitialized) {
            session = object : Session {
                override fun isLoggedIn(): Boolean {
                    if (getToken() != DEFAULT) {
                        return true
                    }
                    return false
                }

                override fun getToken(): String {
                    return mPreferences.getString(TOKEN, DEFAULT)!!
                }

                override fun getEmail(): String {
                    return mPreferences.getString(EMAIL, DEFAULT)!!
                }

                override fun getPassword(): String {
                    return mPreferences.getString(EMAIL, DEFAULT)!!
                }

                override fun saveToken(token: String) {
                    val preferencesEditor = mPreferences.edit()
                    preferencesEditor.putString(TOKEN, token)
                    preferencesEditor.apply()
                }

                override fun saveEmail(email: String) {
                    val preferencesEditor = mPreferences.edit()
                    preferencesEditor.putString(EMAIL, email)
                    preferencesEditor.apply()
                }

                override fun savePassword(password: String) {
                    val preferencesEditor = mPreferences.edit()
                    preferencesEditor.putString(PASSWORD, password)
                    preferencesEditor.apply()
                }

                override fun invalidate() {
                    val preferencesEditor = mPreferences.edit()
                    preferencesEditor.clear().apply()
                    if (::authenticationListener.isInitialized) {
                        authenticationListener.onUserLoggedOut()
                    }
                }

            }
        }

        return session
    }

    private fun isTokenExpired(): Boolean {
        //TODO: check if token is expired while user start's the app
        return false
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

        okhttpClientBuilder.authenticator { route, response ->
            //sync call
            val execute =
                getApiService().loginAccount(Utils.getAuthorizationHeader(session.getEmail(), session.getPassword()))
                    .execute()
            if (execute.isSuccessful) {
                execute.body()?.token?.let {
                    session.saveToken(it)
                    return@authenticator response.request().newBuilder()
                        .header("Auth-Token", it)
                        .build()
                }
            }
            null
        }
        return okhttpClientBuilder.build()
    }
}
