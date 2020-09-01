package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.TopUp
import java.lang.String
import java.util.*

class TopupListAdapter(private val topUpItemClickListener: OnTopUpItemClickListener) :
    RecyclerView.Adapter<TopupListAdapter.TopupListViewHOlder>() {
    private var topUpList: List<TopUp>? = ArrayList<TopUp>()

    interface OnTopUpItemClickListener {
        fun onTopUpItemClick(topUp: TopUp?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopupListViewHOlder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        return TopupListViewHOlder(view)
    }

    override fun onBindViewHolder(
        holder: TopupListViewHOlder,
        position: Int
    ) {
        if (topUpList != null) {
            val topUp: TopUp = topUpList!![position]
            holder.amountTextView.setText(String.valueOf(topUp.rs))
            holder.validityTv.text = "Validity: " + topUp.validity
            holder.descriptionTv.setText(topUp.desc)
        }
    }

    override fun getItemCount(): Int {
        return if (topUpList != null) {
            topUpList!!.size
        } else {
            0
        }
    }

    fun setTopUpList(topUpList: List<TopUp>?) {
        this.topUpList = topUpList
        notifyDataSetChanged()
    }

    inner class TopupListViewHOlder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView
        override fun onClick(v: View) {
            topUpItemClickListener.onTopUpItemClick(topUpList!![adapterPosition])
        }

        init {
            amountTextView = itemView.findViewById(R.id.plan_amount_textView)
            validityTv = itemView.findViewById(R.id.plan_validty_tv)
            descriptionTv = itemView.findViewById(R.id.plan_des_tv)
            itemView.setOnClickListener(this)
        }
    }

}