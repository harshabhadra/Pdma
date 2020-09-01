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

class SplashViewModel : ViewModel() {

    private val apiService = Api.retrofitService

    //Initializing ViewModel Job
    private val viewModelJob = Job()

    //Initializing coroutines scope
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Store Log in Response
    private var _loginResponseMutableLiveData = MutableLiveData<LoginResponse>()
    val loginResponseLiveData:LiveData<LoginResponse>
    get() = _loginResponseMutableLiveData

    //Store sign up response
    private var _signUpResponseMutableLiveData = MutableLiveData<SignUpResponse>()
    val signUpResponseLiveData:LiveData<SignUpResponse>
    get() =  _signUpResponseMutableLiveData

    init {
        _loginResponseMutableLiveData.value = null
        _signUpResponseMutableLiveData.value = null
    }

    //Log in user
    fun loginUser(loginRequest:LoginRequest){

        uiScope.launch {
            val getResponseDeferred = apiService.loginAsync(loginRequest)
            try {
                _loginResponseMutableLiveData.value = getResponseDeferred.await()
                Timber.e("Login response received successfully")
            }catch (e:Exception){
                Timber.e("Failed to get login response : ${e.message}")
            }
        }
    }

    //Sign up user
    fun signUpUser(signUpRequest: SignUpRequest){
        uiScope.launch {
            val getSignUpResponseMessage = apiService.signUpAsync(signUpRequest)
            try {
                val response = getSignUpResponseMessage.await()
                _signUpResponseMutableLiveData.value = response
                Timber.e("Create Account Successfully: ${response.message}")
            }catch (e:Exception){
                Timber.e("Failed to create Account: ${e.message}")
            }
        }
    }
    fun doneLoginNavigation(){
        _loginResponseMutableLiveData.value = null
    }

    fun doneSignUpNavigation(){
        _signUpResponseMutableLiveData.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}