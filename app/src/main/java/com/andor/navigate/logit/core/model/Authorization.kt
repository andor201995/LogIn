package com.andor.navigate.logit.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Authorization {

    @SerializedName("token")
    @Expose
    var token: String? = null

}