package com.pdma.pdma.ui.aeps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.databinding.BankListItemBinding
import com.pdma.pdma.network.BankDetail

class BankListAdapter(val clickListener: BankListItemClickListener) :
    ListAdapter<BankDetail, BankListAdapter.BankListViewHolder>(BankListDiffUtilCallBack()) {

    class BankListViewHolder(private val binding: BankListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bank: BankDetail, clickListener: BankListItemClickListener) {
            binding.bankdetails = bank
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): BankListViewHolder {

                val binding = BankListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BankListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListViewHolder {
        return BankListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BankListViewHolder, position: Int) {
        val bank = getItem(position)
        holder.bind(bank, clickListener)
    }
}

class BankListItemClickListener(val clickListener: (bank: BankDetail) -> Unit) {
    fun onBankListItemClick(bank: BankDetail) = clickListener(bank)
}

class BankListDiffUtilCallBack : DiffUtil.ItemCallback<BankDetail>() {
    override fun areItemsTheSame(oldItem: BankDetail, newItem: BankDetail): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: BankDetail, newItem: BankDetail): Boolean {
        return oldItem.id == newItem.id
    }

}