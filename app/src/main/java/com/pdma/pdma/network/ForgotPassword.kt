package com.pdma.pdma.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class SendOtp(
    @JsonClass(generateAdapter = true)
    val user_id:String,
    @JsonClass(generateAdapter = true)
    val auth_key:String,
    @JsonClass(generateAdapter = true)
    val purpose:String
)


data class OtpResponse (
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String?,
    @Json(name = "data")
    var data: OtpData
)

data class OtpData (
    @Json(name = "otp")
    var otp: String
)

data class ForgotPasswordRequest(
    @JsonClass(generateAdapter = true)
    val user_id:String,
    @JsonClass(generateAdapter = true)
    val auth_key:String,
    @JsonClass(generateAdapter = true)
    val password:String
)

data class ForgotPasswordResponse(
    @Json(name = "status")
    val status:String,
    @Json(name = "message")
    val message:String
)