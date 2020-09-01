package com.pdma.pdma.ui.recharge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.databinding.OperatorListItemBinding
import com.pdma.pdma.network.RechargeOperator

class OperatorListAdapter(private val clickListener: OperatorListItemClickListener) :
    ListAdapter<RechargeOperator, OperatorListAdapter.OperatorListViewHolder>(
        OperatorListDiffUtilCallBack()
    ) {

    class OperatorListViewHolder private constructor(private val binding: OperatorListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rechargeOperator: RechargeOperator,clickListener: OperatorListItemClickListener) {
            binding.operator = rechargeOperator
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): OperatorListViewHolder {

                val binding = OperatorListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return OperatorListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperatorListViewHolder {
        return OperatorListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OperatorListViewHolder, position: Int) {

        val operator = getItem(position)
        operator?.let {
            holder.bind(operator,clickListener)
        }
    }
}

class OperatorListItemClickListener(val clickListener: (rechargeOperator: RechargeOperator) -> Unit) {
    fun onOperatorListItemClick(rechargeOperator: RechargeOperator) =
        clickListener(rechargeOperator)
}

class OperatorListDiffUtilCallBack : DiffUtil.ItemCallback<RechargeOperator>() {
    override fun areItemsTheSame(oldItem: RechargeOperator, newItem: RechargeOperator): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: RechargeOperator, newItem: RechargeOperator): Boolean {

        return oldItem.id == newItem.id
    }

}