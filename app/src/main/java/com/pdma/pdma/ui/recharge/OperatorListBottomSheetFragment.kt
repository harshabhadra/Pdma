package com.pdma.pdma.ui.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pdma.pdma.databinding.FragmentOperatorListBottomSheetBinding
import com.pdma.pdma.network.RechargeOperator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class OperatorListBottomSheetFragment(onOperatorListItemClickListener: OnOperatorListItemClickListener) :
    BottomSheetDialogFragment() {

    private lateinit var viewModel: MobileViewModel
    private lateinit var operatorListBinding: FragmentOperatorListBottomSheetBinding
    private lateinit var operatorListAdapter: OperatorListAdapter
    private var onOperatorListItemClickListener = onOperatorListItemClickListener

    interface OnOperatorListItemClickListener {
        fun onOperatorItemClick(rechargeOperator: RechargeOperator)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        operatorListBinding =
            FragmentOperatorListBottomSheetBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(MobileViewModel::class.java)

        viewModel.getMobileOperators()

        operatorListAdapter = OperatorListAdapter(OperatorListItemClickListener {
            onOperatorListItemClickListener.onOperatorItemClick(it)
            dismiss()
        })
        operatorListBinding.operatorRecyclerView.adapter = operatorListAdapter

        //Observe operator list
        viewModel.operatorListLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                operatorListBinding.operatorProgressbar.visibility = View.GONE
                operatorListAdapter.submitList(it)
            }
        })
        return operatorListBinding.root
    }

}