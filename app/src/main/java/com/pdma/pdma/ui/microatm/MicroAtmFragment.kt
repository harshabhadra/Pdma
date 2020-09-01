package com.pdma.pdma.ui.microatm

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pdma.pdma.BuildConfig
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.databinding.MicroAtmFragmentBinding
import com.pdma.pdma.domain.Checksum
import com.pdma.pdma.network.LoginResponse
import com.pdma.pdma.network.MaRequest
import com.easypay.epmoney.epmoneylib.baseframework.model.PaisaNikalConfiguration
import com.easypay.epmoney.epmoneylib.baseframework.model.PaisaNikalRequest
import com.easypay.epmoney.epmoneylib.response_model.PaisaNikalTransactionResponse
import com.easypay.epmoney.epmoneylib.ui.activity.IntermidiateActivity
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig.ApiServices
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig.ApiTransactionId
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig.Config.Companion.ENVIRONMENT_PRODUCTION
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig.Config.Companion.PAISA_NIKAL_CONFIG
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig.Config.Companion.PAISA_NIKAL_REQUEST
import org.apache.commons.codec.DecoderException
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*

class MicroAtmFragment : Fragment() {

    companion object {
        const val CONFIG_ENVIRONMENT: String = ENVIRONMENT_PRODUCTION
        const val CONFIG_AGENT_ID_CODE = "FORMAX01" //RS00789
        const val CONFIG_AGENT_NMAE = "Ezazul Haque" //Agent name
        const val CODE_MICRO_TRANSACTION = 1011
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 1012
    }

    private lateinit var config: PaisaNikalConfiguration
    private lateinit var apiRequest: PaisaNikalRequest

    private lateinit var mobile: String
    private lateinit var amount: String
    private lateinit var orderId: String

    private lateinit var microAtmFragmentBinding: MicroAtmFragmentBinding
    private lateinit var viewModel: MicroAtmViewModel
    private var isBalanceEnquiry = true

    private lateinit var loginResponse: LoginResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        microAtmFragmentBinding = MicroAtmFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MicroAtmViewModel::class.java)
        val activity = activity as HomeActivity
        loginResponse = activity.getLoginResponse()

        if (foregroundPermissionApproved()) {
            getLocationDetails()
        } else {
            requestForegroundPermissions()
        }

        config = PaisaNikalConfiguration()
        config.agentId = CONFIG_AGENT_ID_CODE
        config.agentName = CONFIG_AGENT_NMAE
        config.environment = CONFIG_ENVIRONMENT

        //Set checkChanged listener to radio group
        microAtmFragmentBinding.microRadioGroup.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.micro_balance_radio_button) {
                isBalanceEnquiry = true
                microAtmFragmentBinding.microAmountTextInput.isEnabled = false
            } else {
                isBalanceEnquiry = false
                microAtmFragmentBinding.microAmountTextInput.isEnabled = true
            }
        }

        //Set onClickListener to proceed button
        microAtmFragmentBinding.microProceedButton.setOnClickListener {
            mobile = microAtmFragmentBinding.microMobileTextInput.text.toString()
            if (mobile.isEmpty()) {
                Toast.makeText(requireContext(), "Enter Mobile number", Toast.LENGTH_SHORT).show()
            } else {
                if (isBalanceEnquiry) {
                    viewModel.getMaResponse(
                        MaRequest(
                            loginResponse.data.id, "12345", "BE", mobile, ""
                        )
                    )
                } else {
                    amount = microAtmFragmentBinding.microAmountTextInput.text.toString()
                    if (amount.isEmpty()) {
                        Toast.makeText(requireContext(), "Enter Amount", Toast.LENGTH_SHORT).show()
                    } else if (!isValidAmount(amount)) {
                        Toast.makeText(
                            requireContext(),
                            "Enter amount between 100 to 10000",
                            Toast.LENGTH_SHORT
                        ).show()    
                    } else {
                        viewModel.getMaResponse(
                            MaRequest(
                                loginResponse.data.id, "12345", "CW", mobile, amount
                            )
                        )
                    }
                }
            }
        }

        //Observe maResponse
        viewModel.maResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.e("Order id: ${it.data.orderId}")
                orderId = it.data.orderId
                Toast.makeText(requireContext(), "Order id: ${it.data.orderId}", Toast.LENGTH_SHORT)
                    .show()
                if (isBalanceEnquiry) {
                    balanceEnquiry()
                } else {
                    cashWithDrawl()
                }
            }
        })

        return microAtmFragmentBinding.root
    }

    private fun cashWithDrawl() {

        //TODO do as same as AEPS balance inquiry request
        //Aeps withdrawal cash request
        apiRequest = PaisaNikalRequest()
        apiRequest.amount = amount //Transaction Amount

        try {
            apiRequest.checkSum = Checksum.getCheckSum(CONFIG_AGENT_ID_CODE)
        } catch (e: DecoderException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        val sb = java.lang.StringBuilder()
        sb.append(ApiTransactionId.MATM_CREDIT_DEBIT_TRANSACTION)
        sb.append(System.currentTimeMillis())

        apiRequest.mobileNumber = mobile
        apiRequest.orderId = orderId
        apiRequest.timeStemp = Checksum.currentTime.toString()
        apiRequest.token = Checksum.randomNumber
        apiRequest.serviceCode = ApiServices.MATM_CREDIT_DEBIT_TRANSACTION.toString()

        val intent = Intent(activity, IntermidiateActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(PAISA_NIKAL_CONFIG, config)
        bundle.putParcelable(PAISA_NIKAL_REQUEST, apiRequest)
        intent.putExtras(bundle)
        startActivityForResult(intent, CODE_MICRO_TRANSACTION)
    }

    private fun balanceEnquiry() {

        apiRequest = PaisaNikalRequest()
        apiRequest.amount = "0" //Transaction Amount

        try {
            apiRequest.checkSum = Checksum.getCheckSum(CONFIG_AGENT_ID_CODE)
        } catch (e: DecoderException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        val sb = StringBuilder()
        sb.append(ApiTransactionId.MATM_CARD_BALANCE_INQUIRY)
        sb.append(System.currentTimeMillis())

        apiRequest.mobileNumber = mobile
        apiRequest.orderId = orderId
        apiRequest.timeStemp = Checksum.currentTime.toString()
        apiRequest.token = Checksum.randomNumber
        apiRequest.serviceCode = ApiServices.MATM_CARD_BALANCE_INQUIRY.toString()


        val intent = Intent(activity, IntermidiateActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(PAISA_NIKAL_CONFIG, config)
        bundle.putParcelable(PAISA_NIKAL_REQUEST, apiRequest)
        intent.putExtras(bundle)
        startActivityForResult(intent, CODE_MICRO_TRANSACTION)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_OK) {
            if (data != null && data.extras != null) {
                val bundle = data.extras
                if (bundle != null) {
                    val apiResponse: PaisaNikalTransactionResponse =
                        bundle.getParcelable(PaisaNikalConfig.UI.FINO_TRANSACTION_RESPONSE)!!
                    Log.e("TAG", "Response: $apiResponse")
                    if (apiResponse != null && apiResponse.rESPCODE == 200) {
                        //Success handler
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        //Fail handler
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_CANCELED) {
            //handler for user canceled
            Toast.makeText(
                requireContext(),
                "Request has been canceled by user",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Timber.e("User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission was granted.
                    Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT)
                        .show()
                    getLocationDetails()
                }
                else -> {
                    // Permission denied
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage(getString(R.string.permission_denied_explanation))
                    builder.setCancelable(false)
                    builder.setPositiveButton(
                        "Settings",
                        DialogInterface.OnClickListener { dialog, which ->
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            dialog.dismiss()
                        })
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(getString(R.string.permission_rationale))
            builder.setCancelable(false)
            builder.setPositiveButton(getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, which ->
                    // Request permission
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                    dialog.dismiss()
                })
            builder.create().show()
        } else {
            Timber.e("Request foreground only permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationDetails() {

        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (isGpsEnabled) {
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {

                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addressList = geocoder.getFromLocation(it.latitude, it.longitude, 1)

            }
        } else {
            //Create an alert dialog if the location is off on device
            val builder =
                AlertDialog.Builder(requireContext())

            builder.setCancelable(false)
            builder.setMessage("Your Location is disabled. Turn on your location")
            builder.setPositiveButton(
                "ok"
            ) { dialog, which -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun isValidAmount(amount: String): Boolean {
        val a = amount.toDouble()
        return a in 100.0..10000.0
    }
}