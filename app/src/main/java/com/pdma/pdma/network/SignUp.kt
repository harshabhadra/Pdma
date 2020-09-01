package com.pdma.pdma.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignUpRequest(
    @JsonClass(generateAdapter = true)
    val business_name: String,
    @JsonClass(generateAdapter = true)
    val name: String,
    @JsonClass(generateAdapter = true)
    val mobile: String,
    @JsonClass(generateAdapter = true)
    val email: String,
    @JsonClass(generateAdapter = true)
    val address: String
) : Parcelable

data class SignUpResponse(
    @Json(name = "status")
    val status: String,
    @Json(name = "message")
    val message: String
)