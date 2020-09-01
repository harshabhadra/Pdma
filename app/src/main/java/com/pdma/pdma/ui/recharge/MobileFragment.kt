package com.pdma.pdma.ui.recharge

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.databinding.MobileFragmentBinding
import com.pdma.pdma.network.LoginResponse
import com.pdma.pdma.network.RechargeOperator
import com.pdma.pdma.network.RechargeRequest

class MobileFragment : Fragment(), OperatorListBottomSheetFragment.OnOperatorListItemClickListener {

    private lateinit var viewModel: MobileViewModel
    private lateinit var mobileFragmentBinding: MobileFragmentBinding

    private lateinit var mobileNumber: String
    private lateinit var operatorName: String
    private lateinit var operatorId: String
    private lateinit var amount: String
    private var isPrepaid: Boolean = true

    private lateinit var loginResponse: LoginResponse

    companion object{
        const val PICK_CONTACT = 101
        const val PERMISSION_REQUEST_CONTACTS = 102
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Inflating Layout
        mobileFragmentBinding = MobileFragmentBinding.inflate(inflater, container, false)

        //Initializing ViewModel class
        viewModel = ViewModelProvider(this).get(MobileViewModel::class.java)

        val activity = requireActivity() as HomeActivity
        loginResponse = activity.getLoginResponse()

        requestContactPermission()

        mobileFragmentBinding.selectTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.prepaid_radio_button -> {
                    isPrepaid = true
                    mobileFragmentBinding.browsePlansText.isEnabled = true
                }
                else -> {
                    isPrepaid = false
                    mobileFragmentBinding.browsePlansText.isEnabled = false
                }
            }
        }

        //Set on click listener to contact image
        mobileFragmentBinding.contactImage.setOnClickListener {
            getContact()
        }

        //Set onClickListener to recharge button
        mobileFragmentBinding.rechargeButton.setOnClickListener {
            mobileNumber = mobileFragmentBinding.mobileNumberTextInput.text.toString()
            amount = mobileFragmentBinding.amountTextInput.text.toString()

            when {
                mobileNumber.isEmpty() -> Toast.makeText(
                    requireContext(),
                    getString(R.string.enter_mobile),
                    Toast.LENGTH_SHORT
                ).show()
                amount.isEmpty() -> Toast.makeText(
                    requireContext(),
                    getString(R.string.enter_amount),
                    Toast.LENGTH_SHORT
                ).show()
                operatorId.isEmpty() -> Toast.makeText(
                    requireContext(),
                    getString(R.string.select_operator),
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    val confirmationBottomSheet = RechcargeBottomSheetFragment(
                        RechargeRequest(
                            loginResponse.data.id,
                            "12345",
                            operatorId,
                            mobileNumber,
                            amount
                        )
                    )
                    confirmationBottomSheet.isCancelable = false
                    confirmationBottomSheet.show(
                        requireActivity().supportFragmentManager,
                        confirmationBottomSheet.tag
                    )
                }
            }
        }

        //Set onClickListener to select operator tv
        mobileFragmentBinding.providerName.setOnClickListener {
            val operatorBottomSheet = OperatorListBottomSheetFragment(this)
            operatorBottomSheet.show(requireActivity().supportFragmentManager, operatorBottomSheet.tag)
        }
        return mobileFragmentBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            val contactData = data!!.data
            var number = ""
            val cursor =
                requireActivity().contentResolver.query(contactData!!, null, null, null, null)
            cursor!!.moveToFirst()
            val hasPhone =
                cursor!!.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
            val contactId =
                cursor!!.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            if (hasPhone == "1") {
                val phones = requireActivity().contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = " + contactId, null, null
                )
                while (phones!!.moveToNext()) {
                    number =
                        phones!!.getString(phones!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            .replace("[-() ]".toRegex(), "").trim { it <= ' ' }
                    if (number.length > 10 && number.startsWith("+91")) {
                        number = number.substring(3)
                        mobileFragmentBinding.mobileNumberTextInput.setText(number)
                    } else {
                        mobileFragmentBinding.mobileNumberTextInput.setText(number)
                    }
                }
                phones!!.close()
                //Do something with number
            } else {
                Toast.makeText(
                    requireContext(),
                    "This contact has no phone number",
                    Toast.LENGTH_LONG
                ).show()
            }
            cursor.close()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
            } else {
                // Permission request was denied.
                requestContactPermission()
            }
        }
    }
    override fun onOperatorItemClick(rechargeOperator: RechargeOperator) {
        mobileFragmentBinding.providerName.text = rechargeOperator.operatorName
        operatorId = rechargeOperator.id
    }

    //Open Contacts to get Mobile Number
    private fun getContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent,PICK_CONTACT)
    }

    private fun requestContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_CONTACTS
            )
        ) {
            val builder =
                AlertDialog.Builder(requireContext())
            builder.setTitle("Contact Permission Required")
            builder.setMessage("To recharge number from your saved contacts")
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Ok"
            ) { dialog, which ->
                dialog.dismiss()
                getPermission()
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            getPermission()
        }
    }

    private fun getPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSION_REQUEST_CONTACTS
        )
    }
}