package com.pdma.pdma.ui.aeps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pdma.pdma.R
import com.pdma.pdma.databinding.FragmentAepsResponseBinding
import com.pdma.pdma.network.MiniStatement

class AepsResponseFragment : Fragment() {

    private lateinit var aepsResponseFragmentBinding: FragmentAepsResponseBinding
    private lateinit var ministateMentListAdapter: MinistateMentListAdapter
    private var miniSateMentList: List<MiniStatement>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        aepsResponseFragmentBinding =
            FragmentAepsResponseBinding.inflate(inflater, container, false)

        val arguments = AepsResponseFragmentArgs.fromBundle(requireArguments())
        val aeps = arguments.aepsResponse

        aepsResponseFragmentBinding.aeps = aeps

        miniSateMentList = aeps.data.miniStatement

        setResponseAnimation(aeps.status)

        //Setting up miniStateMent Recyclerview
        ministateMentListAdapter = MinistateMentListAdapter()
        aepsResponseFragmentBinding.miniStatementRecyclerView.adapter = ministateMentListAdapter
        miniSateMentList?.let {
            ministateMentListAdapter.submitList(it)
        }

        aepsResponseFragmentBinding.okButton.setOnClickListener {

            findNavController().navigate(AepsResponseFragmentDirections.actionAepsResponseFragmentToNavHome())
        }
        return aepsResponseFragmentBinding.root
    }

    private fun setResponseAnimation(status: String) {
        when (status) {
            "SUCCESS" -> aepsResponseFragmentBinding.aepsLottieAnimationView.setAnimation(R.raw.success)
            "FAILED" -> aepsResponseFragmentBinding.aepsLottieAnimationView.setAnimation(R.raw.failed)
            else -> aepsResponseFragmentBinding.aepsLottieAnimationView.setAnimation(R.raw.pending)
        }
    }
}