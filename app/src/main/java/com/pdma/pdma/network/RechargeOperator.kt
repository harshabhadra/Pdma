package com.pdma.pdma.network

import com.squareup.moshi.Json

data class RechargeOperator(
    @Json(name = "id")
    var id: String,
    @Json(name = "operator_type")
    var operatorType: String,
    @Json(name = "operator_name")
    var operatorName: String,
    @Json(name = "operator_code")
    var operatorCode: String,
    @Json(name = "amount_range")
    var amountRange: String,
    @Json(name = "bbps_enabled")
    var bbpsEnabled: String,
    @Json(name = "bill_fetch")
    var billFetch: String,
    @Json(name = "status")
    var status: String
)