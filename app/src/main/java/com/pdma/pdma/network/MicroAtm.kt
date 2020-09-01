package com.pdma.pdma.network

import com.squareup.moshi.Json
data class MaRequest(
    @Json(name = "id")
    var id: String,
    @Json(name = "auth_key")
    var authKey: String,
    @Json(name = "service_type")
    var serviceType: String,
    @Json(name = "mobile")
    var mobile: String,
    @Json(name = "amount")
    var amount: String
)

data class MaResponse(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: MaData
)

data class MaData (
    @Json(name = "order_id")
    var orderId: String
)