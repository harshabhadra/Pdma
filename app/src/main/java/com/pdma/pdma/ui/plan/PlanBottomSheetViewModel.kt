package com.pdma.pdma.ui.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pdma.pdma.network.Api
import com.pdma.pdma.network.RechargePlans
import com.pdma.pdma.network.Roffer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PlanBottomSheetViewModel : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val apiService = Api.planService

    private var _planMutableLiveData = MutableLiveData<RechargePlans>()
    val planLiveData: LiveData<RechargePlans>
        get() = _planMutableLiveData

    private var _rOfferMutableLiveData = MutableLiveData<Roffer>()
    val rOfferLiveData: LiveData<Roffer>
        get() = _rOfferMutableLiveData

    init {
        _rOfferMutableLiveData.value = null
        _planMutableLiveData.value = null
    }

    //Get recharge plans
    fun getRechargePlans(
        apiMemberId: String,
        apiPassword: String,
        circleCode: String,
        operatorCode: String
    ) {
        uiScope.launch {

            apiService.getPlanDetails(apiMemberId, apiPassword, circleCode, operatorCode)
                .enqueue(object : Callback<RechargePlans> {
                    override fun onFailure(call: Call<RechargePlans>, t: Throwable) {
                        Timber.e("Failed to get recharge plans: ${t.message}")
                        _planMutableLiveData.value = null
                    }

                    override fun onResponse(
                        call: Call<RechargePlans>,
                        response: Response<RechargePlans>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                Timber.e("Recharge plans received successfully")
                                _planMutableLiveData.value = it
                            } ?: let {
                                Timber.e("Response body is null")
                                _planMutableLiveData.value = null
                            }
                        } else {
                            Timber.e("Response is unsuccessful: ${response.errorBody().toString()}")
                            _planMutableLiveData.value = null
                        }
                    }

                })
        }
    }

    //Get R offer
    fun getRoffer(
        apiMemberId: String,
        apiPassword: String,
        operatorCode: Int,
        mobile: String
    ) {
        uiScope.launch {

            apiService.getRoffer(apiMemberId, apiPassword, operatorCode, mobile)
                .enqueue(object : Callback<Roffer> {
                    override fun onFailure(call: Call<Roffer>, t: Throwable) {
                        Timber.e("Failed to get roffer : ${t.message}")
                        _rOfferMutableLiveData.value = null
                    }

                    override fun onResponse(call: Call<Roffer>, response: Response<Roffer>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                Timber.e("Successfully received R offers")
                                _rOfferMutableLiveData.value = it
                            } ?: let {
                                Timber.e("Response is empty")
                                _rOfferMutableLiveData.value = null
                            }
                        } else {
                            Timber.e("Response is unsuccessful: ${response.errorBody().toString()}")
                            _rOfferMutableLiveData.value = null
                        }
                    }
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}