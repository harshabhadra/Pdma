package com.pdma.pdma.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.databinding.FragmentLoginBinding
import com.pdma.pdma.network.LoginRequest
import com.pdma.pdma.utils.Constants
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var loginFragmentBinding:FragmentLoginBinding
    private lateinit var viewModel:SplashViewModel
    private lateinit var userId:String
    private lateinit var password:String
    private lateinit var loadingDialog:AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        loginFragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)

        //Initializing ViewModel class
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        //Set on click listener to log in button
        loginFragmentBinding.signInButton.setOnClickListener {
            userId = loginFragmentBinding.loginUserIdTextInput.text.toString()
            password = loginFragmentBinding.loginPasswordTextInput.text.toString()

            if (userId.isEmpty()){
                loginFragmentBinding.loginUserIdTextInputLayout.error = getString(R.string.enter_user_id)
            }else if (password.isEmpty()){
                loginFragmentBinding.loginPasswordTextInputLayout.error = getString(R.string.enter_password)
            }else{
                loadingDialog = createLoadingDialog(requireContext())
                loadingDialog.show()
                Timber.e("loggin in user")
                viewModel.loginUser(LoginRequest(userId, password))
            }
        }

        //Observe Login response from ViewModel class
        viewModel.loginResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog.dismiss()
                Timber.e("Log in Response: ${it.status}")
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                if (it.status == getString(R.string.success)) {
                    val sharedPreferences = requireActivity().getSharedPreferences(
                        Constants.LOGIN_CREDENTIAL,
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPreferences.edit()
                    editor.putString(Constants.USER_ID, userId)
                    editor.putString(Constants.PASSWORD, password)
                    editor.apply()

                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    intent.putExtra(Constants.LOGIN_RESPONSE, it)
                    startActivity(intent)
                    requireActivity().finish()
                    viewModel.doneLoginNavigation()
                }
            }
        })

        //Set on click listener to forget password
        loginFragmentBinding.forgotPasswordTextView.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgetPassFragment())
        }

        //set on click listener to create account button
        loginFragmentBinding.loginSignUpButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSingUpFragment())
        }

        //Add TextWatcher to user Id and password TextInput
        loginFragmentBinding.loginUserIdTextInput.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                loginFragmentBinding.loginUserIdTextInputLayout.isErrorEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginFragmentBinding.loginUserIdTextInputLayout.isErrorEnabled = false
            }
        })

        //Add TextWatcher to Password TextInput
        loginFragmentBinding.loginPasswordTextInput.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                loginFragmentBinding.loginPasswordTextInputLayout.isErrorEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginFragmentBinding.loginPasswordTextInputLayout.isErrorEnabled = false
            }
        })
        return loginFragmentBinding.root
    }

    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }
}