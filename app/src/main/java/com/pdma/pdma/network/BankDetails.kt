package com.pdma.pdma.network

import com.squareup.moshi.Json

data class BankDetail (
    @Json(name = "id")
    var id: String,
    @Json(name = "bank_name")
    var bankName: String,
    @Json(name = "bank_sort_name")
    var bankSortName: String,
    @Json(name = "branch_ifsc")
    var branchIfsc: String,
    @Json(name = "ifsc_alias")
    var ifscAlias: String,
    @Json(name = "imps_enabled")
    var impsEnabled: String,
    @Json(name = "aeps_enabled")
    var aepsEnabled: String,
    @Json(name = "bank_iin")
    var bankIin: String,
    @Json(name = "is_down")
    var isDown: String
)