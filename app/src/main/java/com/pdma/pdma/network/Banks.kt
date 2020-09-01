package com.pdma.pdma.network

import com.squareup.moshi.Json

data class Banks(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: List<List<BankData>>
)

data class BankData(
    @Json(name = "id")
    var id: String,
    @Json(name = "user_id")
    var userId: String,
    @Json(name = "name_in_bank")
    var nameInBank: String,
    @Json(name = "bank_name")
    var bankName: String,
    @Json(name = "account_type")
    var accountType: String,
    @Json(name = "account_no")
    var accountNo: String,
    @Json(name = "ifsc_code")
    var ifscCode: String,
    @Json(name = "branch")
    var branch: String,
    @Json(name = "status")
    var status: String,
    @Json(name = "created_on")
    var createdOn: String,
    @Json(name = "modified_on")
    var modifiedOn: String
)