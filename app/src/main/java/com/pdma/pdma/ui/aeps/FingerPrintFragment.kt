package com.pdma.pdma.ui.aeps

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.databinding.FragmentFingerPrintBinding
import com.pdma.pdma.network.LoginResponse
import org.json.JSONException
import org.json.XML
import timber.log.Timber

const val DEVICE_INFO = 100
const val AUTHENTICATION_REQUEST = 101

class FingerPrintFragment : Fragment() {

    private lateinit var fingerPrintBinding: FragmentFingerPrintBinding
    private lateinit var viewModel: AepsViewModel

    private lateinit var loadingDialog: AlertDialog

    private lateinit var loginResponse: LoginResponse

    private lateinit var type: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fingerPrintBinding = FragmentFingerPrintBinding.inflate(inflater, container, false)

        //Initializing ViewModel class
        viewModel = ViewModelProvider(this).get(AepsViewModel::class.java)

        val activity = requireActivity() as HomeActivity

        loginResponse = activity.getLoginResponse()

        val arguments = FingerPrintFragmentArgs.fromBundle(requireArguments())
        val aeps = arguments.aepsinput
        fingerPrintBinding.aeps = aeps

        checkDeviceInfo()
        checkStartTekDeviceInfo()
        type = getType(aeps.type)

        //Set onClickListener to capture Finger print button
        fingerPrintBinding.captureFingerprintButton.setOnClickListener {
            getAuthenticationInfo()
        }

        //Observing fingerprint response
        viewModel.fingerPrintResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog = createLoadingDialog(requireContext())
                loadingDialog.show()
                Timber.e("id:${loginResponse.data.id}")
                Timber.e("mobile: ${aeps.mobile}")
                Timber.e("aadhar: ${aeps.aadhar}")
                Timber.e("bank: ${aeps.bankici}")
                Timber.e("type: ${type}")
                Timber.e("pidData: ${it.pidData}")

                viewModel.getAepsResponse(
                    loginResponse.data.id,
                    type,
                    aeps.amount.toString(),
                    aeps.aadhar,
                    aeps.bankici,
                    aeps.mobile,
                    it.pidDataType,
                    it.pidData,
                    it.ci,
                    it.dc,
                    it.dpId,
                    it.errCode,
                    it.errInfo,
                    it.fCount,
                    it.tType,
                    it.hmac,
                    it.iCount,
                    it.mc,
                    it.mi,
                    it.nmPoints,
                    it.pCount,
                    it.pType,
                    it.qScore,
                    it.rdsId,
                    it.rdsVer,
                    it.sessionKey,
                    it.srno
                )
            }
        })

        //Observing Aeps response
        viewModel.aepsTransactionResponse.observe(viewLifecycleOwner, Observer {

            it?.let {
                loadingDialog.dismiss()
                Toast.makeText(requireContext(), it.status, Toast.LENGTH_LONG).show()
                Timber.e("messag: ${it.message}")
                Timber.e("messag: ${it.data.serviceType}")

                findNavController().navigate(
                    FingerPrintFragmentDirections.actionFingerPrintFragmentToAepsResponseFragment(
                        it
                    )
                )
            }
        })
        return fingerPrintBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DEVICE_INFO) {
            if (resultCode == RESULT_OK) {
                val b = data!!.extras
                if (b != null) {
                    val deviceInfo = b.getString("DEVICE_INFO", "")
                    val rdServiceInfo = b.getString("RD_SERVICE_INFO", "")
                    val dnc = b.getString("DNC", "")
                    val dnr = b.getString("DNR", "")
                    if (!dnc.isEmpty() || !dnr.isEmpty()) {
                        showLogInfoDialog(
                            "Device Info",
                            "$dnc"
                        )
                    } else {
                        showLogInfoDialog("Device Info", deviceInfo + rdServiceInfo)
                    }
                }
            }
        } else if (requestCode == AUTHENTICATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                val b = data!!.extras
                if (b != null) {
                    val pidData =
                        b.getString("PID_DATA")
                    // in this varaible you will get Pid data String dnc = b.getString("DNC", "");
                    // you will get value in this variable when your finger print device not connected
                    val dnr = b.getString(
                        "DNR",
                        ""
                    )

                    pidData?.let {
                        convertXmlToJson(it)
                    }
                    // you will get value in this variable when your finger print device not registered.

                }
            }
        }
    }

    //Show dialog that device is not connected
    private fun showLogInfoDialog(title: String, message: String) {
        val builder =
            AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(
            "Ok"
        ) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    //Check device info
    private fun checkDeviceInfo() {
        if (isPackageInstalled("com.scl.rdservice", requireActivity().packageManager)) {
            val intent = Intent("in.gov.uidai.rdservice.fp.INFO")
            intent.setPackage("com.scl.rdservice")
            startActivityForResult(intent, DEVICE_INFO)
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(false)
            builder.setMessage("You need to download Morpho SCL RDService app to capture Fingerprint")
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + "com.scl.rdservice")
                    )
                )
                dialog.dismiss()
            })

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun checkStartTekDeviceInfo() {
        val intentInfo = Intent("in.gov.uidai.rdservice.fp.INFO")
        val packageManager = requireActivity().packageManager
        val activities =
            packageManager.queryIntentActivities(intentInfo, PackageManager.MATCH_DEFAULT_ONLY)
        val isIntentSafe = activities.size > 0;
        if (!isIntentSafe) {
//"No Finger Scanner Support available"
            Toast.makeText(requireContext(), "intent safe", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "intent not safe", Toast.LENGTH_SHORT).show()
        }
    }

    //Get Authentication information from intent
    private fun getAuthenticationInfo() {
        val responseXml = "<?xml version=\"1.0\"?><PidOptions ver=\"1.0\">" +
                "<Opts fCount=\"1\" fType=\"0\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\"" +
                " timeout=\"10000\" posh=\"UNKNOWN\" env=\"P\" /><CustOpts></CustOpts></PidOptions>"

        val intent = Intent("in.gov.uidai.rdservice.fp.CAPTURE")
        intent.setPackage("com.scl.rdservice")
        intent.putExtra("PID_OPTIONS", responseXml)
        startActivityForResult(intent, AUTHENTICATION_REQUEST)
    }

    //Check if an app is installed in app
    private fun isPackageInstalled(
        packageName: String,
        packageManager: PackageManager
    ): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun convertXmlToJson(response: String) {

        try {
            val jsonObject = XML.toJSONObject(response)
            jsonObject?.let {
                viewModel.getFingerPrintResponse(it)
            }
        } catch (e: JSONException) {
            Log.e("FingerPrintFragment", "Error: ${e.message}")
        }
    }

    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }

    fun getType(name: String): String {
        return when (name) {
            getString(R.string.cash_withdrawal) -> "CW"
            getString(R.string.balance_enquiry) -> "BE"
            else -> "MS"
        }
    }
}