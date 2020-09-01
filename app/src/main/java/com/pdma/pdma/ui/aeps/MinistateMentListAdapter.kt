package com.pdma.pdma.ui.aeps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.databinding.MiniStatementListItemBinding
import com.pdma.pdma.network.MiniStatement

class MinistateMentListAdapter :
    ListAdapter<MiniStatement, MinistateMentListAdapter.MinistateMentListViewHolder>(
        MinistateMentDiffUtilCallBack()
    ) {

    class MinistateMentListViewHolder private constructor(val binding: MiniStatementListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(miniStatement: MiniStatement) {
            binding.ministatement = miniStatement
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MinistateMentListViewHolder {
                val binding = MiniStatementListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MinistateMentListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinistateMentListViewHolder {
        return MinistateMentListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MinistateMentListViewHolder, position: Int) {
        val minisatatement = getItem(position)
        minisatatement?.let {
            holder.bind(it)
        }
    }
}

class MinistateMentDiffUtilCallBack : DiffUtil.ItemCallback<MiniStatement>() {
    override fun areItemsTheSame(oldItem: MiniStatement, newItem: MiniStatement): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MiniStatement, newItem: MiniStatement): Boolean {
        return oldItem.date == newItem.date
    }

}