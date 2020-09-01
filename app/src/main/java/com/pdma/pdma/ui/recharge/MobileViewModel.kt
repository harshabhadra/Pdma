package com.pdma.pdma.ui.recharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.network.Api
import com.pdma.pdma.network.RechargeOperator
import com.pdma.pdma.network.RechargeRequest
import com.pdma.pdma.network.RechargeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

private var mobileOperatorList: MutableList<RechargeOperator> = mutableListOf()

class MobileViewModel : ViewModel() {

    private val apiService = Api.retrofitService

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Store recharge response
    private var _rechargeResponseMutableLiveData = MutableLiveData<RechargeResponse>()
    val rechargeResponseLiveData: LiveData<RechargeResponse>
        get() = _rechargeResponseMutableLiveData

    //Store operator list
    private var _operatorListMutableLiveData = MutableLiveData<List<RechargeOperator>>()
    val operatorListLiveData: LiveData<List<RechargeOperator>>
        get() = _operatorListMutableLiveData

    init {
        _operatorListMutableLiveData.value = null
        _rechargeResponseMutableLiveData.value = null
    }

    fun doRecharge(rechargeRequest: RechargeRequest) {

        uiScope.launch {

            val rechargeResponseDeferred = apiService.doRechargeAsync(rechargeRequest)
            try {
                _rechargeResponseMutableLiveData.value = rechargeResponseDeferred.await()
            } catch (e: Exception) {
                Timber.e("Failed to get recharge response: ${e.message}")
                _rechargeResponseMutableLiveData.value = null
            }
        }
    }

    fun getMobileOperators() {
        uiScope.launch {

            val operatorListDeferred = apiService.getOperatorsAsync()
            try {
                val operatorList = operatorListDeferred.await()
                for (operator in operatorList) {
                    if (operator.operatorType == "Prepaid" || operator.operatorType == "Postpaid") {
                        mobileOperatorList.add(operator)
                    }
                }
                _operatorListMutableLiveData.value = mobileOperatorList
            } catch (e: Exception) {
                Timber.e("Failed to get operators")
                _operatorListMutableLiveData.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}