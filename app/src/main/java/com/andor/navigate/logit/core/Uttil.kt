package com.andor.navigate.logit.core

import android.util.Base64

class Utils {
    companion object {

        /**
         * this method is API implemetation specific
         * might not work with other APIs
         */
        fun getAuthorizationHeader(email: String, password: String): String {
            val credential = "$email:$password"
//            return "Basic " + Base64.encodeToString(credential.toByteArray(), Base64.DEFAULT)
            return "anmol"
        }
    }
}