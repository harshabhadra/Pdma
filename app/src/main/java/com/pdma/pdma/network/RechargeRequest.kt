package com.pdma.pdma.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class RechargeRequest(
    @JsonClass(generateAdapter = true)
    val id:String,
    @JsonClass(generateAdapter = true)
    val auth_key:String,
    @JsonClass(generateAdapter = true)
    val operator_id:String,
    @JsonClass(generateAdapter = true)
    val number:String,
    @JsonClass(generateAdapter = true)
    val amount:String
)

data class RechargeResponse(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: RechargeData
)

data class RechargeData(
    @Json(name = "order_id")
    var orderId: String,
    @Json(name = "opt_txn_id")
    var optTxnId: String
)