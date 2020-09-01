package com.pdma.pdma.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

data class LoginRequest(
    @JsonClass(generateAdapter = true)
    val user_id:String,
    @JsonClass(generateAdapter = true)
    val password:String
)

@Parcelize
data class LoginResponse (
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: Data
):Parcelable

@Parcelize
data class Data (
    @Json(name = "id")
    var id: String
):Parcelable