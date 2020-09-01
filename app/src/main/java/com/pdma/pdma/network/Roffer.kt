package com.pdma.pdma.network

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Roffer(
    @SerializedName("ERROR")
    @Expose
    var eRROR: String,
    @SerializedName("STATUS")
    @Expose
    var sTATUS: String,
    @SerializedName("MOBILENO")
    @Expose
    var mOBILENO: String,
    @SerializedName("RDATA")
    @Expose
    var rDATA: List<Rdata>,
    @SerializedName("MESSAGE")
    @Expose
    var mESSAGE: String
):Parcelable

@Parcelize
data class Rdata(
    @SerializedName("price")
    @Expose
    var price: String,
    @SerializedName("commissionUnit")
    @Expose
    var commissionUnit: String,
    @SerializedName("ofrtext")
    @Expose
    var ofrtext: String,
    @SerializedName("logdesc")
    @Expose
    var logdesc: String,
    @SerializedName("commissionAmount")
    @Expose
    var commissionAmount: String
):Parcelable