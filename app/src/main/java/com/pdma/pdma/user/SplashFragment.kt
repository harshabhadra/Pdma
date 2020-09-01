package com.pdma.pdma.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.network.LoginRequest
import com.pdma.pdma.utils.Constants

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        // TODO: Use the ViewModel

        val sharedPreference =
            requireActivity().getSharedPreferences(Constants.LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
        val userId = sharedPreference.getString(Constants.USER_ID, "")
        val password = sharedPreference.getString(Constants.PASSWORD, "")
        Handler().postDelayed({

            if (!userId.isNullOrEmpty() && !password.isNullOrEmpty()) {
                if (userId != "" && password != "") {
                    viewModel.loginUser(LoginRequest(userId, password))
                }
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            }
        }, 2000)

        //Observe Login Response
        viewModel.loginResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.status == getString(R.string.success)) {
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    intent.putExtra(Constants.LOGIN_RESPONSE, it)
                    startActivity(intent)
                    requireActivity().finish()
                }else{
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                }
            }
        })
    }

}