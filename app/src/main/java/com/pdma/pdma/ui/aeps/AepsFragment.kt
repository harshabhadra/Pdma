package com.pdma.pdma.ui.aeps

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pdma.pdma.R
import com.pdma.pdma.databinding.AepsFragmentBinding
import com.pdma.pdma.domain.AepsInput
import com.pdma.pdma.network.BankDetail
import kotlinx.android.synthetic.main.choose_device_layout.view.*

class AepsFragment : Fragment(), BankBottomSheetFragment.OnBankItemClickListener {

    private lateinit var aepsFragmentBinding: AepsFragmentBinding
    private lateinit var viewModel: AepsViewModel

    private lateinit var mobileNumber: String
    private lateinit var aadharNumber: String
    private lateinit var bank: String
    private lateinit var tType: String
    private lateinit var amount: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Inflating the layout
        aepsFragmentBinding = AepsFragmentBinding.inflate(inflater, container, false)

        val arguments = AepsFragmentArgs.fromBundle(requireArguments())
        tType = arguments.category.catName

        if (tType == getString(R.string.cash_withdrawal)) {
            aepsFragmentBinding.aepsAmountLayout.visibility = View.VISIBLE
        } else {
            aepsFragmentBinding.aepsAmountLayout.visibility = View.GONE
        }

        //Set on click listener to select bank textView
        aepsFragmentBinding.selectBankTextView.setOnClickListener {
            val bankListBottomSheetFragment = BankBottomSheetFragment(this)
            bankListBottomSheetFragment.isCancelable = false
            bankListBottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                bankListBottomSheetFragment.tag
            )
        }

        //Set on click listener to Submit button
        aepsFragmentBinding.aepsSubmitButton.setOnClickListener {

            if (tType == getString(R.string.cash_withdrawal)) {
                mobileNumber = aepsFragmentBinding.aepsMobileNumberTextInput.text.toString()
                aadharNumber = aepsFragmentBinding.aadharNumberTextInput.text.toString()
                amount = aepsFragmentBinding.aepsAmountTextInput.text.toString()

                if (mobileNumber.isEmpty()) {
                    aepsFragmentBinding.aepsMobileNumberLayout.error = "Enter Valid Mobile Number"
                } else if (aadharNumber.isEmpty()) {
                    aepsFragmentBinding.aadharNumberTextInputLayout.error =
                        "Enter Valid Aadhar Number"
                } else if (bank.isEmpty()) {
                    Toast.makeText(requireContext(), "Select a bank", Toast.LENGTH_SHORT).show()
                } else if (amount.isEmpty()) {
                    aepsFragmentBinding.aepsAmountLayout.error = "Enter Valid Amount"
                } else {
                    val aepsInput = AepsInput(tType, mobileNumber, aadharNumber, bank, amount)

                    val layout = LayoutInflater.from(requireContext())
                        .inflate(R.layout.choose_device_layout, null)
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setView(layout)
                    val mantraImage = layout.mantra_imageView
                    val morphoImage = layout.morpho_imageView

                    val dialog = builder.create()
                    dialog.show()

                    mantraImage.setOnClickListener {

                        findNavController().navigate(
                            AepsFragmentDirections.actionAepsFragmentToMantraFragment(
                                aepsInput
                            )
                        )
                        dialog.dismiss()
                    }

                    morphoImage.setOnClickListener {

                        findNavController().navigate(
                            AepsFragmentDirections.actionAepsFragmentToFingerPrintFragment(
                                aepsInput
                            )
                        )
                        dialog.dismiss()
                    }
                }
            } else {
                mobileNumber = aepsFragmentBinding.aepsMobileNumberTextInput.text.toString()
                aadharNumber = aepsFragmentBinding.aadharNumberTextInput.text.toString()
                if (mobileNumber.isEmpty()) {
                    aepsFragmentBinding.aepsMobileNumberLayout.error = "Enter Valid Mobile Number"
                } else if (aadharNumber.isEmpty()) {
                    aepsFragmentBinding.aadharNumberTextInputLayout.error =
                        "Enter Valid Aadhar Number"
                } else if (bank.isEmpty()) {
                    Toast.makeText(requireContext(), "Select a bank", Toast.LENGTH_SHORT).show()
                } else {
                    val aepsInput = AepsInput(tType, mobileNumber, aadharNumber, bank)
                    val layout = LayoutInflater.from(requireContext())
                        .inflate(R.layout.choose_device_layout, null)
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setView(layout)
                    val mantraImage = layout.mantra_imageView
                    val morphoImage = layout.morpho_imageView

                    val dialog = builder.create()
                    dialog.show()

                    mantraImage.setOnClickListener {

                        findNavController().navigate(
                            AepsFragmentDirections.actionAepsFragmentToMantraFragment(
                                aepsInput
                            )
                        )
                        dialog.dismiss()
                    }

                    morphoImage.setOnClickListener {

                        findNavController().navigate(
                            AepsFragmentDirections.actionAepsFragmentToFingerPrintFragment(
                                aepsInput
                            )
                        )
                        dialog.dismiss()
                    }
                }
            }
        }

        //Add TextWatcher to TextInputEditTexts
        aepsFragmentBinding.aepsMobileNumberTextInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                aepsFragmentBinding.aepsMobileNumberLayout.isErrorEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aepsFragmentBinding.aepsMobileNumberLayout.isErrorEnabled = false
            }
        })

        aepsFragmentBinding.aadharNumberTextInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                aepsFragmentBinding.aadharNumberTextInputLayout.isErrorEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aepsFragmentBinding.aadharNumberTextInputLayout.isErrorEnabled = false
            }
        })

        aepsFragmentBinding.aepsAmountTextInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                aepsFragmentBinding.aepsAmountLayout.isErrorEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aepsFragmentBinding.aepsAmountLayout.isErrorEnabled = false
            }
        })
        return aepsFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AepsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onBankItemClick(bankDetail: BankDetail) {
        aepsFragmentBinding.selectBankTextView.setText(bankDetail.bankName)
        bank = bankDetail.bankIin
        aepsFragmentBinding.aepsSubmitButton.isEnabled = true
    }

}