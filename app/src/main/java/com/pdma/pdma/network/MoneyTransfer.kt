package com.pdma.pdma.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class MoneyTransfer(
    @JsonClass(generateAdapter = true)
    val id:String,
    @JsonClass(generateAdapter = true)
    val auth_key:String,
    @JsonClass(generateAdapter = true)
    val mobile:String,
    @JsonClass(generateAdapter = true)
    val account:String,
    @JsonClass(generateAdapter = true)
    val name:String,
    @JsonClass(generateAdapter = true)
    val ifsc:String,
    @JsonClass(generateAdapter = true)
    val amount:String
)


data class MoneyTransferResponse(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: MTData
)

data class MTData(
    @Json(name = "order_id")
    var orderId: String
)