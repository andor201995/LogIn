package com.andor.navigate.logit.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserModel {

    @SerializedName("name")
    @Expose
    var name: String? = null

}
