package com.pdma.pdma.ui.moneytransfer

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
import androidx.navigation.fragment.findNavController
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.databinding.MoneyTransfrFragmentBinding
import com.pdma.pdma.network.LoginResponse
import com.pdma.pdma.network.MoneyTransfer

class MoneyTransfrFragment : Fragment() {

    private lateinit var viewModel: MoneyTransfrViewModel
    private lateinit var moneyTransferFragmetBinding: MoneyTransfrFragmentBinding
    private lateinit var moneyTransfer: MoneyTransfer
    private lateinit var loginResponse: LoginResponse

    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moneyTransferFragmetBinding =
            MoneyTransfrFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MoneyTransfrViewModel::class.java)
        val activity = activity as HomeActivity
        loginResponse = activity.getLoginResponse()

        moneyTransferFragmetBinding.moneyTransferButton.setOnClickListener {

            val mobile = moneyTransferFragmetBinding.mtMobileTextInput.text.toString()
            val account = moneyTransferFragmetBinding.mtAccountNumberTextInput.text.toString()
            val name = moneyTransferFragmetBinding.mtNameTextInput.text.toString()
            val ifsc = moneyTransferFragmetBinding.mtIfscTextInput.text.toString()
            val amount = moneyTransferFragmetBinding.mtAmountTextInput.text.toString()

            when {
                mobile.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "Enter Mobile number",
                    Toast.LENGTH_SHORT
                ).show()
                account.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "Enter account number",
                    Toast.LENGTH_SHORT
                ).show()
                name.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "Enter Account Holder Name",
                    Toast.LENGTH_SHORT
                ).show()
                ifsc.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "Enter IFSC Code",
                    Toast.LENGTH_SHORT
                ).show()
                amount.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "Enter Amount",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    moneyTransfer = MoneyTransfer(
                        loginResponse.data.id,
                        "12345",
                        mobile,
                        account,
                        name,
                        ifsc,
                        amount
                    )
                    viewModel.moneyTrasfer(moneyTransfer)
                    loadingDialog = createLoadingDialog(requireContext())
                    loadingDialog.show()
                }
            }

        }

        //Observing money transfer livedata
        viewModel.moneyTransferLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog.dismiss()
                Toast.makeText(requireContext(), "Status: ${it.message}", Toast.LENGTH_SHORT).show()
                findNavController().navigate(MoneyTransfrFragmentDirections.actionMoneyTransfrFragmentSelf())
            }
        })
        return moneyTransferFragmetBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }
}