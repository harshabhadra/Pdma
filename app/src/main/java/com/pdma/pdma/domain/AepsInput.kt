package com.pdma.pdma.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AepsInput(
    val type:String,
    val mobile:String,
    val aadhar:String,
    val bankici:String,
    val amount:String? = ""
):Parcelable

data class FingerPrintResponse(
    var pidDataType: String,
    var pidData: String,
    var ci: String,
    var dc: String,
    var dpId: String,
    var errCode: String,
    var errInfo: String,
    var fCount: String,
    var tType: String,
    var hmac: String,
    var iCount: String,
    var mc: String,
    var mi: String,
    var nmPoints: String,
    var pCount: String,
    var pType: String,
    var qScore: String,
    var rdsId: String,
    var rdsVer: String,
    var sessionKey: String,
    var srno: String
)