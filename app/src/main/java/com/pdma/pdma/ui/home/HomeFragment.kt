package com.pdma.pdma.ui.home

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.HomeActivity
import com.pdma.pdma.R
import com.pdma.pdma.domain.CategoryItem
import com.pdma.pdma.network.LoginResponse
import com.pdma.pdma.network.MoveToBank
import com.pdma.pdma.network.ProfileRequest
import com.pdma.pdma.network.ProfileResponse
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.move_to_bank_layout.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeCategoryAdapter: HomeCategoryAdapter
    private lateinit var loadingDialog: AlertDialog
    private lateinit var loginResponse: LoginResponse
    private lateinit var progressBar: ProgressBar
    private lateinit var profileResponse: ProfileResponse

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val needPermissionList: MutableSet<String> = mutableSetOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val walletTv: TextView = root.wallet_balance_tv

        val activity = activity as HomeActivity
        loginResponse = activity.getLoginResponse()

        //Setting up wallet
        homeViewModel.checkBalance(ProfileRequest(loginResponse.data.id, "12345"))
        homeViewModel.getUserProfile(ProfileRequest(loginResponse.data.id, "12345"))

        loadingDialog = createLoadingDialog(requireContext())
        loadingDialog.show()

        //Observe wallet balance
        homeViewModel.walletBalanceLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingDialog.dismiss()
                val balance = "Wallet Balance:  Rs/- ${it.data.bal.toString()}"
                walletTv.text = balance
            }
        })

        //Observe profile response
        homeViewModel.profileLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                profileResponse = it
                Toast.makeText(
                    requireContext(),
                    it.data[0].businessName.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val catList = mutableListOf<CategoryItem>()

        catList.add(CategoryItem(R.drawable.cash_withdrawl, getString(R.string.cash_withdrawal)))
        catList.add(CategoryItem(R.drawable.balance_check, getString(R.string.balance_enquiry)))
        catList.add(CategoryItem(R.drawable.mini_statement, getString(R.string.mini_statement)))
        catList.add(
            CategoryItem(
                R.drawable.ic_baseline_money_transfer,
                getString(R.string.money_transfer)
            )
        )
        catList.add(
            CategoryItem(
                R.drawable.ic_baseline_mobile_recharge,
                getString(R.string.mobile_recharge)
            )
        )
        catList.add(CategoryItem(R.drawable.ic_baseline_atm, getString(R.string.micro_atm)))

        val categoryRecycler: RecyclerView = root.findViewById(R.id.category_recycler)
        homeCategoryAdapter = HomeCategoryAdapter(HomeCategoryClickListener {
            navigate(it)
        })

        categoryRecycler.adapter = homeCategoryAdapter
        homeCategoryAdapter.submitList(catList)

        val mTbTv = root.findViewById<TextView>(R.id.move_to_bank_tv)
        mTbTv.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.
                actionNavHomeToMoveToBankFragment(profileResponse.data[0]))
        }

        homeViewModel.mTbResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        return root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }

    private fun navigate(category: CategoryItem) {

        when (category.catName) {
            getString(R.string.money_transfer) -> {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToMoneyTransfrFragment())
            }
            getString(R.string.mobile_recharge) -> {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToMobileFragment())
            }
            getString(R.string.micro_atm) -> {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToMicroAtmFragment())
            }
            else -> findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToAepsFragment(
                    category
                )
            )
        }
    }

    private fun createMtbDialog() {
        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.move_to_bank_layout, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(layout)
        val dialog = builder.create()
        val amountEt = layout.mtb_amount_textInput
        progressBar = layout.mtb_progressBar
        val button = layout.mtb_proceed_button

        button.setOnClickListener {
            val mTbAmount = amountEt.text.toString()
            if (mTbAmount.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                homeViewModel.moveToBank(
                    MoveToBank(
                        loginResponse.data.id,
                        "12345",
                        "NEFT",
                        mTbAmount
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Enter Amount", Toast.LENGTH_SHORT).show()
            }
        }
        layout.mtb_close_imageView.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createLoadingDialog(context: Context): AlertDialog {
        val layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layout)
        builder.setCancelable(false)
        return builder.create()
    }
}