package com.pdma.pdma.network

import com.squareup.moshi.Json

data class WalletBalance(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: WalletData
)

data class WalletData(
    @Json(name = "bal")
    var bal: Double
)