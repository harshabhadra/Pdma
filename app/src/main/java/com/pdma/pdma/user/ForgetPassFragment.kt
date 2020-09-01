package com.pdma.pdma.user

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
import com.pdma.pdma.R
import com.pdma.pdma.databinding.ForgetPassFragmentBinding
import com.pdma.pdma.network.ForgotPasswordRequest
import com.pdma.pdma.network.SendOtp

class ForgetPassFragment : Fragment() {

    companion object {
        fun newInstance() = ForgetPassFragment()
    }

    private lateinit var viewModel: ForgetPassViewModel
    private lateinit var forgetPasswordBinding: ForgetPassFragmentBinding
    private lateinit var userId:String
    private lateinit var otp:String
    private lateinit var userOtp:String
    private lateinit var loadingDialog: AlertDialog
    private lateinit var sendOtpDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        forgetPasswordBinding = ForgetPassFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ForgetPassViewModel::class.java)

        forgetPasswordBinding.sendOtpButton.setOnClickListener {
            userId = forgetPasswordBinding.fpUserIdTextInput.text.toString()
            if (userId.isEmpty()){
                Toast.makeText(requireContext(), "Enter User id", Toast.LENGTH_SHORT).show()
            }else{
                sendOtpDialog = createLoadingDialog(requireContext())
                sendOtpDialog.show()
                viewModel.getOtp(SendOtp(userId,"12345","reset password"))

            }
        }

        viewModel.otpResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                sendOtpDialog.dismiss()
                forgetPasswordBinding.fpOtpGroup.visibility = View.VISIBLE
                forgetPasswordBinding.fpUserIdTextInputLayout.visibility = View.GONE
                forgetPasswordBinding.sendOtpButton.visibility = View.GONE
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                forgetPasswordBinding.otpView.setText(it.data.otp)
                otp = it.data.otp
            }
        })

        forgetPasswordBinding.otpView.setOtpCompletionListener {
            it?.let {
                userOtp = it
            }
        }

        forgetPasswordBinding.otpVerifyButton.setOnClickListener {
            if (userOtp == otp){
                forgetPasswordBinding.fpOtpGroup.visibility = View.GONE
                forgetPasswordBinding.fpPasswordGroup.visibility = View.VISIBLE
                viewModel.otpDone()
            }else{
                Toast.makeText(requireContext(),"Invalid OTP", Toast.LENGTH_LONG).show()
            }
        }

        forgetPasswordBinding.resetPasswordButton.setOnClickListener {
            val password = forgetPasswordBinding.fpNewPasswordTextInput.text.toString()
            val confirmPass = forgetPasswordBinding.fpConfirmPasswordTextInput.text.toString()

            when {
                password.isEmpty() -> {
                    Toast.makeText(requireContext(),"Enter New Password", Toast.LENGTH_SHORT).show()
                }
                confirmPass.isEmpty() -> {
                    Toast.makeText(requireContext(),"Confirm you password", Toast.LENGTH_SHORT).show()
                }
                password != confirmPass -> {
                    Toast.makeText(requireContext(),"Password Mismatch", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.resetPassword(ForgotPasswordRequest(userId,"12345", password))
                    loadingDialog = createLoadingDialog(requireContext())
                    loadingDialog.show()
                }
            }
        }

        viewModel.resetPasswordLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog.dismiss()
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                findNavController().navigate(ForgetPassFragmentDirections.actionForgetPassFragmentToLoginFragment())
                viewModel.doneNavigating()
            }
        })
        return forgetPasswordBinding.root
    }

    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }
}