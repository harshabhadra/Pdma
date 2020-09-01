package com.pdma.pdma.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private val apiService = Api.retrofitService

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Store wallet balance
    private var _walletBalanceMutableLiveData = MutableLiveData<WalletBalance>()
    val walletBalanceLiveData: LiveData<WalletBalance>
        get() = _walletBalanceMutableLiveData

    //Store mtb response
    private var _mTbResponseMutableLiveData = MutableLiveData<MtbResponse>()
    val mTbResponseLiveData: LiveData<MtbResponse>
        get() = _mTbResponseMutableLiveData

    //Store profile response
    private var _profileMutableLiveData = MutableLiveData<ProfileResponse>()
    val profileLiveData: LiveData<ProfileResponse>
        get() = _profileMutableLiveData

    init {
        _walletBalanceMutableLiveData.value = null
        _mTbResponseMutableLiveData.value = null
        _profileMutableLiveData.value = null
    }

    //Check Wallet balance
    fun checkBalance(profileRequest: ProfileRequest) {

        uiScope.launch {
            val balanceDeferred = apiService.checkBalanceAsync(profileRequest)
            try {
                val balance = balanceDeferred.await()
                _walletBalanceMutableLiveData.value = balance
            } catch (e: Exception) {
                Timber.e("Failed to get balance: ${e.message}")
            }
        }
    }

    //Move to bank
    fun moveToBank(moveToBank: MoveToBank) {
        uiScope.launch {
            val mtbResponseDeferred = apiService.moveToBankAsync(moveToBank)
            try {
                val response = mtbResponseDeferred.await()
                _mTbResponseMutableLiveData.value = response
            } catch (e: Exception) {
                Timber.e("Failed to move money to bank ${e.message}")
                _mTbResponseMutableLiveData.value = null
            }
        }
    }

    //Get profile response
    fun getUserProfile(profileRequest: ProfileRequest) {
        uiScope.launch {
            val responseDeferred = apiService.getProfileAsync(profileRequest)
            try {
                val response = responseDeferred.await()
                _profileMutableLiveData.value = response
            } catch (e: Exception) {
                Timber.e("Failed to get profile response: ${e.message}")
                _profileMutableLiveData.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}