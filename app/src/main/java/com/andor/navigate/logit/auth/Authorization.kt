package com.andor.navigate.logit.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Authorization {

    @SerializedName("token")
    @Expose
    var token: String? = null

}