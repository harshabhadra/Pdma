package com.pdma.pdma.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class MoveToBank(
    @JsonClass(generateAdapter = true)
    val id:String,
    @JsonClass(generateAdapter = true)
    val auth_key:String,
    @JsonClass(generateAdapter = true)
    val mode:String,
    @JsonClass(generateAdapter = true)
    val amount:String
)

data class MtbResponse(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: MTBData
)
data class MTBData(
    @Json(name = "order_id")
    var orderId: String
)