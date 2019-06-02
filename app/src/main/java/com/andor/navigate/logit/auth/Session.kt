package com.andor.navigate.logit.auth

interface Session {
    fun isLoggedIn(): Boolean

    fun getToken(): String

    fun getEmail(): String

    fun getPassword(): String

    fun saveToken(token: String)

    fun saveEmail(email: String)

    fun savePassword(password: String)

    fun invalidate()
}