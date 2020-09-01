package com.pdma.pdma.utils

import android.util.Patterns

class AppUtils {

    companion object {
        //validate email
        fun isValidMail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        //Validate phone
        fun isValidMobile(phone: String): Boolean {
            return Patterns.PHONE.matcher(phone).matches()
        }
    }
}