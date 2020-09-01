package com.pdma.pdma.ui.aeps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.network.Api
import com.pdma.pdma.network.BankDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class BankBottomSheetViewModel : ViewModel() {

    private val apiService = Api.retroService

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _bankListMutableLiveData = MutableLiveData<List<BankDetail>>()
    val bankListLiveData:LiveData<List<BankDetail>>
    get() = _bankListMutableLiveData

    init {
        _bankListMutableLiveData.value = null
    }

    fun getBankList(){
        uiScope.launch {
            val getBankListDeferred = apiService.getBankListAsync()
            try {
                val bankList = getBankListDeferred.await()
                _bankListMutableLiveData.value = bankList
            } catch (e: Exception) {
                Timber.e("Failed To get bank list response: ${e.message}")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}