package com.pdma.pdma.domain

data class FingerPrintData(
    val deviceInfo: DeviceInfo?
)

data class DeviceInfo(
    val dpId:String? = "",
    val rdsId:String? = "",
    val rdsVer:String? = "",
    val mi:String? = "",
    val mc:String?=""
)