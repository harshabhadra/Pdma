package com.pdma.pdma.user

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pdma.pdma.R
import com.pdma.pdma.databinding.FragmentSignUpBinding
import com.pdma.pdma.network.SignUpRequest
import com.pdma.pdma.network.SignUpResponse
import com.pdma.pdma.utils.AppUtils

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {

    private lateinit var signUpBinding:FragmentSignUpBinding
    private lateinit var viewModel: SplashViewModel

    private lateinit var name:String
    private lateinit var businessName:String
    private lateinit var address:String
    private lateinit var email:String
    private lateinit var phone:String

    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signUpBinding = FragmentSignUpBinding.inflate(inflater,container,false)

        //Initializing ViewModel class
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        //Set on click listener to next button
        signUpBinding.signUpNextButton.setOnClickListener {

            name = signUpBinding.signUpNameEditText.text.toString()
            businessName = signUpBinding.signUpBusinessNameEditText.text.toString()
            address = signUpBinding.signUpAddressEditText.text.toString()

            if (name.isEmpty()){
                Toast.makeText(requireContext(),"Enter name", Toast.LENGTH_SHORT).show()
            }else if (businessName.isEmpty()){
                Toast.makeText(requireContext(),"Enter Business name", Toast.LENGTH_SHORT).show()
            }else if (address.isEmpty()){
                Toast.makeText(requireContext(),"Enter Your Address", Toast.LENGTH_SHORT).show()
            }
            else{
                signUpBinding.signUpNameGroup.visibility = View.GONE
                signUpBinding.signUpEmailGroup.visibility = View.VISIBLE
            }
        }

        //Set on Click listener to create account button
        signUpBinding.createAccountButton.setOnClickListener {

            email = signUpBinding.signUpEmailEditText.text.toString()
            phone = signUpBinding.signUpPhoneEditText.text.toString()

            if (email.isEmpty() || !AppUtils.isValidMail(email)){
                Toast.makeText(requireContext(),"Enter Valid Email", Toast.LENGTH_SHORT).show()
            }else if (phone.isEmpty() || !AppUtils.isValidMobile(phone)){
                Toast.makeText(requireContext(),"Enter Valid Phone", Toast.LENGTH_SHORT).show()
            }else{
                loadingDialog = createLoadingDialog(requireContext())
                loadingDialog.show()
                viewModel.signUpUser(SignUpRequest(businessName,name,phone,email,address))
            }
        }

        //Observe Sign up response
        viewModel.signUpResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog.dismiss()
                createSignUpDialog(it)
                viewModel.doneSignUpNavigation()
            }
        })
        return signUpBinding.root
    }

    //Crate Dialog
    private fun createSignUpDialog(signUpResponse: SignUpResponse){
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setMessage(signUpResponse.message)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
            findNavController().navigate(SignUpFragmentDirections.actionSingUpFragmentToLoginFragment())
        }
        val dialog = builder.create()
        dialog.show()
    }

    //Create loading Dialog
    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }
}