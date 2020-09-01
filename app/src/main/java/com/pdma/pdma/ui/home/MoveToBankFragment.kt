package com.pdma.pdma.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.databinding.FragmentMoveToBankBinding
import com.pdma.pdma.network.LoginResponse
import com.pdma.pdma.network.MoveToBank

class MoveToBankFragment : Fragment() {

    private lateinit var moveToBankFragmentBinding: FragmentMoveToBankBinding
    private lateinit var amount: String
    private lateinit var tType: String
    private lateinit var loginResponse: LoginResponse
    private lateinit var viewModel: HomeViewModel
    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        moveToBankFragmentBinding = FragmentMoveToBankBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val arguments = MoveToBankFragmentArgs.fromBundle(requireArguments())
        moveToBankFragmentBinding.profile = arguments.profile

        val activity = activity as HomeActivity
        loginResponse = activity.getLoginResponse()

        //Set onCheckedChangeListener to radio group
        moveToBankFragmentBinding.selectTTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.imps_radio_button -> {
                    tType = "IMPS"
                }
                R.id.neft_radio_button -> {
                    tType = "NEFT"
                }
            }
        }

        //Set on click listener to transfer to bank button
        moveToBankFragmentBinding.transferToBankButton.setOnClickListener {
            amount = moveToBankFragmentBinding.mtbAmountTextInput.text.toString()

            if (amount.isEmpty()) {
                Toast.makeText(requireContext(), "Enter Valid Amount", Toast.LENGTH_LONG).show()
            } else {
                loadingDialog = createLoadingDialog(requireContext())
                loadingDialog.show()
                val moveToBank = MoveToBank(loginResponse.data.id, "12345", tType, amount)
                viewModel.moveToBank(moveToBank)
            }
        }

        //Observe move to bank response from viewModel
        viewModel.mTbResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog.dismiss()
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        })
        return moveToBankFragmentBinding.root
    }

    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }
}