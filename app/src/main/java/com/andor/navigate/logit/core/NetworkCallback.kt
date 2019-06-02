package com.andor.navigate.logit.core

interface NetworkCallback<T> {
    fun onResponse(response: T)

    fun onError(error: String)

    fun type(): Class<T>
}
