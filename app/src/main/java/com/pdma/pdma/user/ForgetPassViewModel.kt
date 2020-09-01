package com.pdma.pdma.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class ForgetPassViewModel : ViewModel() {

    private val apiService = Api.retrofitService

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _otpResponse = MutableLiveData<OtpResponse>()
    val otpResponse:LiveData<OtpResponse>
    get() = _otpResponse

    private var _resetPassMuableLiveData = MutableLiveData<ForgotPasswordResponse>()
    val resetPasswordLiveData:LiveData<ForgotPasswordResponse>
    get() = _resetPassMuableLiveData

    init {
        _otpResponse.value = null
        _resetPassMuableLiveData.value = null
    }

    fun getOtp(sendOtp: SendOtp){
        uiScope.launch {

            val getResponseDeferred = apiService.sendOtpAsync(sendOtp)
            try {
                val otpValue = getResponseDeferred.await()
                _otpResponse.value = otpValue
            }catch (e:Exception){
                Timber.e("Failed to get otp: ${e.message}")
            }
        }
    }

    fun resetPassword(forgotPasswordRequest: ForgotPasswordRequest){
        uiScope.launch {

            val getResponseDeferrd = apiService.resetPasswordAsync(forgotPasswordRequest)
            try {

                val resetPassword = getResponseDeferrd.await()
                _resetPassMuableLiveData.value = resetPassword
            }catch (e:Exception){
                Timber.e("Failed to reset password ${e.message}")
            }
        }
    }

    fun otpDone(){
        _otpResponse.value = null
    }

    fun doneNavigating(){
        _resetPassMuableLiveData.value = null
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}