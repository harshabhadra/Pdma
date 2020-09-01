package com.pdma.pdma.ui.microatm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.network.Api
import com.pdma.pdma.network.MaRequest
import com.pdma.pdma.network.MaResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MicroAtmViewModel : ViewModel() {
    private val apiService = Api.retrofitService

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Store micro atm response
    private var _maResponseMutableLiveData = MutableLiveData<MaResponse>()
    val maResponseLiveData:LiveData<MaResponse>
    get() = _maResponseMutableLiveData

    init {
        _maResponseMutableLiveData.value = null
    }

    fun getMaResponse(maRequest: MaRequest){
        uiScope.launch {
            val maResponseDeferred = apiService.getMaServiceAsync(maRequest)
            try {

                val response = maResponseDeferred.await()
                _maResponseMutableLiveData.value = response

            }catch (e:Exception){
                Timber.e("Failed to get ma response: ${e.message}")
                _maResponseMutableLiveData.value = null
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}