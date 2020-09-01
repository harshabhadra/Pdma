package com.pdma.pdma.ui.recharge

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pdma.pdma.R
import com.pdma.pdma.databinding.FragmentRechcargeBottomSheetBinding
import com.pdma.pdma.network.RechargeRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RechcargeBottomSheetFragment(private val rechargeRequest: RechargeRequest) : BottomSheetDialogFragment() {

    private lateinit var confirmationLayoutBinding:FragmentRechcargeBottomSheetBinding
    private lateinit var mobileViewModel: MobileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        confirmationLayoutBinding = FragmentRechcargeBottomSheetBinding.inflate(inflater,container,false)

        confirmationLayoutBinding.rechargeRequest = rechargeRequest

        //Initializing ViewModel class
        mobileViewModel = ViewModelProvider(this).get(MobileViewModel::class.java)

        //Set on Click listener to cancel button
        confirmationLayoutBinding.confirmCancelButton.setOnClickListener {
            dismiss()
        }

        //Set on Click listener to pay button
        confirmationLayoutBinding.confirmPayButton.setOnClickListener {

            confirmationLayoutBinding.confirmButtonGroup.visibility = View.GONE
            confirmationLayoutBinding.progressBar.visibility = View.VISIBLE
            mobileViewModel.doRecharge(rechargeRequest)
        }

        //Observing Recharge response
        mobileViewModel.rechargeResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                confirmationLayoutBinding.progressBar.visibility = View.GONE
                confirmationLayoutBinding.confirmCredentialGroup.visibility = View.GONE
                if (it.status == "FAILED"){
                    setFailedAnimation("Recharge Failed")
                }else{
                    setDoneAnimation()
                }
            }
        })

        confirmationLayoutBinding.closeConfirmLayoutIv.setOnClickListener{
            dismiss()
        }
        return confirmationLayoutBinding.root
    }

    private fun setFailedAnimation(message: String) {
        confirmationLayoutBinding.showResultGroup.visibility = View.VISIBLE
        confirmationLayoutBinding.lottieAnimationView.setAnimation(R.raw.failed)
        confirmationLayoutBinding.rechargeStatusTextView.text = message
        confirmationLayoutBinding.rechargeStatusTextView.setTextColor(Color.RED)
    }

    private fun setDoneAnimation() {
        confirmationLayoutBinding.showResultGroup.visibility = View.VISIBLE
        confirmationLayoutBinding.lottieAnimationView.setAnimation(R.raw.done)
        confirmationLayoutBinding.rechargeStatusTextView.text = getString(R.string.recharge_succss)
    }
}