package com.pdma.pdma.network

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


data class AepsRequest (
    @SerializedName("id")
    @Expose
    var id: String = "2",
    @SerializedName("service_type")
    @Expose
    var serviceType: String ="CW",
    @SerializedName("amount")
    @Expose
    var amount: String?="",
    @SerializedName("aadhaar")
    @Expose
    var aadhaar: String? = "",
    @SerializedName("bank")
    @Expose
    var bank: String? = "",
    @SerializedName("mobile")
    @Expose
    var mobile: String? ="",
    @SerializedName("pidDataType")
    @Expose
    var pidDataType: String?="",
    @SerializedName("pidData")
    @Expose
    var pidData: String? = "",
    @SerializedName("ci")
    @Expose
    var ci: String? = "",
    @SerializedName("dc")
    @Expose
    var dc: String? = "",
    @SerializedName("dpId")
    @Expose
    var dpId: String? ="",
    @SerializedName("errCode")
    @Expose
    var errCode: String? = "",
    @SerializedName("errInfo")
    @Expose
    var errInfo: String? = "",
    @SerializedName("fCount")
    @Expose
    var fCount: String? = "",
    @SerializedName("tType")
    @Expose
    var tType: String? = "",
    @SerializedName("hmac")
    @Expose
    var hmac: String? = "",
    @SerializedName("iCount")
    @Expose
    var iCount: String? = "",
    @SerializedName("mc")
    @Expose
    var mc: String? = "",
    @SerializedName("mi")
    @Expose
    var mi: String? = "",
    @SerializedName("nmPoints")
    @Expose
    var nmPoints: String? = "",
    @SerializedName("pCount")
    @Expose
    var pCount: String? = "",
    @SerializedName("pType")
    @Expose
    var pType: String? = "",
    @SerializedName("qScore")
    @Expose
    var qScore: String? = "",
    @SerializedName("rdsId")
    @Expose
    var rdsId: String? = "",
    @SerializedName("rdsVer")
    @Expose
    var rdsVer: String? = "",
    @SerializedName("sessionKey")
    @Expose
    var sessionKey: String? = "",
    @SerializedName("srno")
    @Expose
    var srno: String? = ""
)

@Parcelize
data class AepsResponse(
    @Json(name = "status")
    var status: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "data")
    var data: AepsData
):Parcelable

@Parcelize
data class AepsData(
    @Json(name = "status")
    var status: String,
    @Json(name = "service_type")
    var serviceType: String?,
    @Json(name = "mobile")
    var mobile: String,
    @Json(name = "aadhaar")
    var aadhaar: String,
    @Json(name = "bank_name")
    var bankName: String,
    @Json(name = "amount")
    var amount: String,
    @Json(name = "balance")
    var balance: String,
    @Json(name = "order_id")
    var orderId: String,
    @Json(name = "rrn")
    var rrn: String,
    @Json(name = "timestamp")
    var timestamp: String,
    @Json(name = "mini_statement")
    var miniStatement: List<MiniStatement>?,
    @Json(name = "id")
    var id: String
):Parcelable

@Parcelize
data class MiniStatement(
    @Json(name = "date")
    var date:String,
    @Json(name = "txnType")
    var txnType:String,
    @Json(name = "amount")
    var amount:String,
    @Json(name = "narration")
    var narration:String
):Parcelable