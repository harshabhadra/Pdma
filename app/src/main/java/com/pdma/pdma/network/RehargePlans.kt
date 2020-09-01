package com.pdma.pdma.network

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class RechargePlans(
    @SerializedName("ERROR")
    @Expose
    var eRROR: String,
    @SerializedName("STATUS")
    @Expose
    var sTATUS: String,
    @SerializedName("Operator")
    @Expose
    var operator: String,
    @SerializedName("Circle")
    @Expose
    var circle: String,
    @SerializedName("RDATA")
    @Expose
    var rDATA: RequestData,
    @SerializedName("MESSAGE")
    @Expose
    var mESSAGE: String
)

data class RequestData(
    @SerializedName("FULLTT")
    @Expose
    var fULLTT: List<FullTt?>?,
    @SerializedName("TOPUP")
    @Expose
    var tOPUP: List<TopUp>?,

    @SerializedName("DATA")
    @Expose
    var dATA: List<DataPlan>?,
    @SerializedName("SMS")
    @Expose
    var sMS: List<Sms>?,
    @SerializedName("Romaing")
    @Expose
    var romaing: List<Roaming>?,
    @SerializedName("FRC")
    @Expose
    var fRC: List<Frc>?,
    @SerializedName("STV")
    @Expose
    var sTV: List<Stv>?
)

@Parcelize
data class DataPlan(
    @SerializedName("rs")
    @Expose
    var rs: Int,
    @SerializedName("validity")
    @Expose
    var validity: String,
    @SerializedName("desc")
    @Expose
    var desc: String,
    @SerializedName("Type")
    @Expose
    var type: String
) : Parcelable

@Parcelize
data class Frc(
    @SerializedName("rs")
    @Expose
    var rs: Int?,
    @SerializedName("validity")
    @Expose
    var validity: String,
    @SerializedName("desc")
    @Expose
    var desc: String,
    @SerializedName("Type")
    @Expose
    var type: String
) : Parcelable

@Parcelize
data class FullTt(
    @SerializedName("rs")
    @Expose
    var rs: Int?,
    @SerializedName("validity")
    @Expose
    var validity: String,
    @SerializedName("desc")
    @Expose
    var desc: String,
    @SerializedName("Type")
    @Expose
    var type: String
) : Parcelable

@Parcelize
data class Roaming(
    @SerializedName("rs")
    @Expose
    var rs: Int?,
    @SerializedName("validity")
    @Expose
    var validity: String,
    @SerializedName("desc")
    @Expose
    var desc: String,
    @SerializedName("Type")
    @Expose
    var type: String
) : Parcelable

@Parcelize
data class Sms(
    @SerializedName("rs")
    @Expose
    var rs: Int?,
    @SerializedName("validity")
    @Expose
    var validity: String,
    @SerializedName("desc")
    @Expose
    var desc: String,
    @SerializedName("Type")
    @Expose
    var type: String
) : Parcelable

@Parcelize
data class Stv(
    @SerializedName("rs")
    @Expose
    var rs: Int?,
    @SerializedName("validity")
    @Expose
    var validity: String,
    @SerializedName("desc")
    @Expose
    var desc: String,
    @SerializedName("Type")
    @Expose
    var type: String
) : Parcelable

@Parcelize
data class TopUp(
    @SerializedName("rs")
    @Expose
    var rs: Int?,
    @SerializedName("validity")
    @Expose
    var validity: String,
    @SerializedName("desc")
    @Expose
    var desc: String,
    @SerializedName("Type")
    @Expose
    var type: String
) : Parcelable