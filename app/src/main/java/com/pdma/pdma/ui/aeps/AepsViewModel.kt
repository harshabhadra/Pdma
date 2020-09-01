package com.pdma.pdma.ui.aeps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.domain.FingerPrintResponse
import com.pdma.pdma.network.AepsResponse
import com.pdma.pdma.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

class AepsViewModel : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val apiService = Api.retrofitService

    private var _fingerPrintResponse = MutableLiveData<FingerPrintResponse>()
    val fingerPrintResponse: LiveData<FingerPrintResponse>
        get() = _fingerPrintResponse

    private var _mantraFingerPrintResponse = MutableLiveData<FingerPrintResponse>()
    val mantraFingerPrintResponse: LiveData<FingerPrintResponse>
        get() = _mantraFingerPrintResponse

    private var _aepsTransactionResponse = MutableLiveData<AepsResponse>()
    val aepsTransactionResponse: LiveData<AepsResponse>
        get() = _aepsTransactionResponse

    init {
        _fingerPrintResponse.value = null
        _mantraFingerPrintResponse.value = null
        _aepsTransactionResponse.value = null
    }

    fun getFingerPrintResponse(jsonObject: JSONObject) {

        var errorInfo = ""
        uiScope.launch {

            try {
                val pidDataObj = jsonObject.getJSONObject("PidData")
                val dataObj = pidDataObj.getJSONObject("Data")
                val sKeyObj = pidDataObj.getJSONObject("Skey")
                val deviceInfoObj = pidDataObj.getJSONObject("DeviceInfo")
                val respObj = pidDataObj.getJSONObject("Resp")

                val dataType = dataObj.getString("type")
                val data = dataObj.getString("content")

                val ci = sKeyObj.getLong("ci")
                val sessionKey = sKeyObj.get("content")

                val dc = deviceInfoObj.getString("dc")
                val dpId = deviceInfoObj.getString("dpId")
                val mc = deviceInfoObj.get("mc")
                val mi = deviceInfoObj.get("mi")
                val rdsId = deviceInfoObj.get("rdsId")
                val rdsVar = deviceInfoObj.get("rdsVer")

                val errorCode = respObj.getInt("errCode")
                if (errorCode == 0){
                    errorInfo = "Success"
                }else{
                    errorInfo = ""
                }
//                errorInfo = respObj.getString("errInfo")

                val fCount = respObj.get("fCount")
                val fType = respObj.get("fType")
                val nmPoints = respObj.get("nmPoints")
                val qScore = respObj.get("qScore")

                val additionalInfoObj = deviceInfoObj.getJSONObject("additional_info")
                val jsonArray = additionalInfoObj.getJSONArray("Param")
                val srnoObj: JSONObject = jsonArray[0] as JSONObject
                val srNo = srnoObj.get("value")

                val hmac = pidDataObj.get("Hmac")


                val fingerPrintResponse = FingerPrintResponse(
                    dataType,
                    data,
                    ci.toString(),
                    dc,
                    dpId,
                    errorCode.toString(),
                    errorInfo,
                    fCount.toString(),
                    "",
                    hmac.toString(),
                    "0",
                    mc.toString(),
                    mi.toString(),
                    nmPoints.toString(),
                    "0",
                    "",
                    qScore.toString(),
                    rdsId.toString(),
                    rdsVar.toString(),
                    sessionKey.toString(),
                    srNo.toString()
                )

                _fingerPrintResponse.value = fingerPrintResponse
            } catch (e: JSONException) {

                Timber.e("Error parsing json : ${e.message}")
            }
        }
    }

    fun getMantraFingerPrintResponse(jsonObject: JSONObject) {

        var errorInfo = ""
        uiScope.launch {

            try {
                val pidDataObj = jsonObject.getJSONObject("PidData")
                val dataObj = pidDataObj.getJSONObject("Data")
                val sKeyObj = pidDataObj.getJSONObject("Skey")
                val deviceInfoObj = pidDataObj.getJSONObject("DeviceInfo")
                val respObj = pidDataObj.getJSONObject("Resp")

                val dataType = dataObj.getString("type")
                val data = dataObj.getString("content")

                val ci = sKeyObj.getLong("ci")
                val sessionKey = sKeyObj.get("content")

                val dc = deviceInfoObj.getString("dc")
                val dpId = deviceInfoObj.getString("dpId")
                val mc = deviceInfoObj.get("mc")
                val mi = deviceInfoObj.get("mi")
                val rdsId = deviceInfoObj.get("rdsId")
                val rdsVar = deviceInfoObj.get("rdsVer")

                val errorCode = respObj.getInt("errCode")
//                if (errorCode == 0){
//                    errorInfo = "Success"
//                }else{
//                    errorInfo = ""
//                }
                errorInfo = respObj.getString("errInfo")

                val fCount = respObj.get("fCount")
                val fType = respObj.get("fType")
                val nmPoints = respObj.get("nmPoints")
                val qScore = respObj.get("qScore")

                val additionalInfoObj = deviceInfoObj.getJSONObject("additional_info")
                val jsonArray = additionalInfoObj.getJSONArray("Param")
                val srnoObj: JSONObject = jsonArray[0] as JSONObject
                val srNo = srnoObj.get("value")

                val hmac = pidDataObj.get("Hmac")


                val fingerPrintResponse = FingerPrintResponse(
                    dataType,
                    data,
                    ci.toString(),
                    dc,
                    dpId,
                    errorCode.toString(),
                    errorInfo,
                    fCount.toString(),
                    "",
                    hmac.toString(),
                    "0",
                    mc.toString(),
                    mi.toString(),
                    nmPoints.toString(),
                    "0",
                    "",
                    qScore.toString(),
                    rdsId.toString(),
                    rdsVar.toString(),
                    sessionKey.toString(),
                    srNo.toString()
                )

                _mantraFingerPrintResponse.value = fingerPrintResponse
            } catch (e: JSONException) {
                _mantraFingerPrintResponse.value = null
                Timber.e("Error parsing json : ${e.message}")
            }
        }
    }
    fun getAepsResponse(
        id: String,
        service_type: String,
        amount: String,
        aadhaar: String,
        bank: String,
        mobile: String,
        pidDataType: String,
        pidData: String,
        ci: String,
        dc: String,
        dpId: String,
        errCode: String,
        errInfo: String,
        fCount: String,
        tType: String,
        hmac: String,
        iCount: String,
        mc: String,
        mi: String,
        nmPoints: String,
        pCount: String,
        pType: String,
        qScore: String,
        rdsId: String,
        rdsVer: String,
        sessionKey: String,
        srno: String
    ) {
        uiScope.launch {
            val getResponseDeferred = apiService.aepsTransactionAsync(
                id,
                service_type,
                amount,
                aadhaar,
                bank,
                mobile,
                pidDataType,
                pidData,
                ci,
                dc,
                dpId,
                errCode,
                errInfo,
                fCount,
                tType,
                hmac,
                iCount,
                mc,
                mi,
                nmPoints,
                pCount,
                pType,
                qScore,
                rdsId,
                rdsVer,
                sessionKey,
                srno
            )
            try {
                val aepsresponsevalue = getResponseDeferred.await()
                _aepsTransactionResponse.value = aepsresponsevalue
                Timber.e("Aeps Transaction response successful: ${aepsresponsevalue.message}")
            } catch (e: Exception) {
                Timber.e("Aeps Transaction response failed: ${e.message}")
            }
        }

//        apiService.aepsTransactionAsync(aepsRequest).enqueue(object :Callback<String>{
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Timber.e("aeps response failed: ${t.message}")
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful && response.body() != null){
//                    Timber.e("aeps response successful: ${response.body().toString()}")
//                }else{
//                    Timber.e("aeps response null: ${response.errorBody()}")
//                }
//            }
//        })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}