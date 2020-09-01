package com.pdma.pdma.ui.aeps

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import com.pdma.pdma.databinding.MantraFragmentBinding
import com.pdma.pdma.network.LoginResponse
import org.json.JSONException
import org.json.XML
import timber.log.Timber

class MantraFragment : Fragment() {

    private lateinit var mantraFragmentBinding:MantraFragmentBinding
    private lateinit var viewModel: AepsViewModel

    private lateinit var loadingDialog: AlertDialog

    private lateinit var loginResponse: LoginResponse

    private lateinit var type: String

    companion object {
        const val MANTRA_DEVICE_INFO = 1101
        const val MANTRA_AUTHENTICATON = 1105
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mantraFragmentBinding = MantraFragmentBinding.inflate(inflater,container,false)

        //Initializing ViewModel class
        viewModel = ViewModelProvider(this).get(AepsViewModel::class.java)

        val activity = requireActivity() as HomeActivity

        loginResponse = activity.getLoginResponse()

        val arguments = MantraFragmentArgs.fromBundle(requireArguments())
        val aeps = arguments.aepsInput
        mantraFragmentBinding.aeps = aeps

        type = getType(aeps.type)

        //Check if device is connected
        checkDeviceInfo()

        //Capture finger print
        mantraFragmentBinding.mantraCaptureFingerprintButton.setOnClickListener {
            getAuthenTicationInfo()
        }

        //Observe Finger print response
        viewModel.mantraFingerPrintResponse.observe(viewLifecycleOwner, Observer {
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

        //Observe aeps response
        viewModel.aepsTransactionResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog.dismiss()
                Toast.makeText(requireContext(), it.status, Toast.LENGTH_LONG).show()
                Timber.e("messag: ${it.message}")
                Timber.e("messag: ${it.data.serviceType}")

                findNavController().navigate(MantraFragmentDirections.actionMantraFragmentToAepsResponseFragment(it))
            }
        })
        return mantraFragmentBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(
            requireContext(),
            "requestCode: $requestCode , resultCode: $resultCode",
            Toast.LENGTH_SHORT
        ).show()
        if (requestCode == MANTRA_DEVICE_INFO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val result = data.getStringExtra("DEVICE_INFO")
                    val rdService = data.getStringExtra("RD_SERVICE_INFO")
                    var display = ""
                    if (rdService != null) {
                        display = "RD Service Info :\n$rdService\n\n"
                    }
                    if (result != null) {
                        /*DeviceInfo info = serializer.read(DeviceInfo.class, result);
                            display = display + "Device Code: " + info.dc + "\n\n"
                                    + "Serial No: " + info.srno + "\n\n"
                                    + "dpId: " + info.dpId + "\n\n"
                                    + "MC: " + info.mc + "\n\n"
                                    + "MI: " + info.mi + "\n\n"
                                    + "rdsId: " + info.rdsId + "\n\n"
                                    + "rdsVer: " + info.rdsVer;*/
                        display += "Device Info :\n$result"
                        Toast.makeText(requireContext(), display, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else if (requestCode == MANTRA_AUTHENTICATON) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val result = data.getStringExtra("PID_DATA")
                    result?.let {
                        Toast.makeText(requireContext(), "Result: $it", Toast.LENGTH_LONG)
                            .show()
                        convertXmlToJson(it)
                    } ?: let {
                        Toast.makeText(requireContext(), "Empty Pid data", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Data is null", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Failed to authenticate", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkDeviceInfo() {
        if (isPackageInstalled("com.mantra.rdservice", requireActivity().packageManager)) {
            val intent = Intent()
            intent.action = "in.gov.uidai.rdservice.fp.INFO"
            intent.`package` = "com.mantra.rdservice"
            startActivityForResult(intent, MANTRA_DEVICE_INFO)
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(false)
            builder.setMessage("You need to download Mantra RDService app to capture Fingerprint")
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + "com.mantra.rdservice")
                    )
                )
                dialog.dismiss()
            })

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun getAuthenTicationInfo() {
        val pidOption = "<PidOptions ver=\"1.0\">" +
                "<Opts env=\"p\" fCount=\"1\" fType=\"0\" format=\"0\" iCount=\"0\" iType=\"0\" pCount=\"0\"" +
                " pType=\"0\" pidVer=\"2.0\" posh=\"UNKNOWN\" timeout=\"10000\"/>" +
                "</PidOptions>"
        val intent = Intent("in.gov.uidai.rdservice.fp.CAPTURE")
        intent.putExtra("PID_OPTIONS", pidOption)
        intent.`package`="com.mantra.rdservice"
        startActivityForResult(intent, MANTRA_AUTHENTICATON)
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

    private fun getType(name: String): String {
        return when (name) {
            getString(R.string.cash_withdrawal) -> "CW"
            getString(R.string.balance_enquiry) -> "BE"
            else -> "MS"
        }
    }

    private fun convertXmlToJson(response: String) {

        try {
            val jsonObject = XML.toJSONObject(response)
            jsonObject?.let {
                viewModel.getMantraFingerPrintResponse(it)
            }
        } catch (e: JSONException) {
            Timber.e("FingerPrintFragment Error: ${e.message}")
        }
    }

    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }

}