package com.pdma.pdma.ui.moneytransfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.network.Api
import com.pdma.pdma.network.MoneyTransfer
import com.pdma.pdma.network.MoneyTransferResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MoneyTransfrViewModel : ViewModel() {

    private val apiService = Api.retrofitService

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _moneyTransferMutableLiveData = MutableLiveData<MoneyTransferResponse>()
    val moneyTransferLiveData:LiveData<MoneyTransferResponse>
    get() = _moneyTransferMutableLiveData

    init {
        _moneyTransferMutableLiveData.value = null
    }

    fun moneyTrasfer(moneyTransfer: MoneyTransfer){

        uiScope.launch {
            val moneyTransferDeferred = apiService.moneyTransferAsync(moneyTransfer)
            try {
                _moneyTransferMutableLiveData.value = moneyTransferDeferred.await()
            } catch (e: Exception) {
                Timber.e("Failed to get money transfer response")
            }
        }
    }

    fun doneNavigating(){
        _moneyTransferMutableLiveData.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}