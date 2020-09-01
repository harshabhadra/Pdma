package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.Roaming
import java.lang.String
import java.util.*

class RoamingListAdapter(private val roamingItemClickListener: OnRoamingItemClickListener) :
    RecyclerView.Adapter<RoamingListAdapter.RoamingListViewHolder>() {
    private var roamingList: List<Roaming>? = ArrayList<Roaming>()

    interface OnRoamingItemClickListener {
        fun onRoamingItemClick(roaming: Roaming?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoamingListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        return RoamingListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RoamingListViewHolder,
        position: Int
    ) {
        if (roamingList != null) {
            val roaming: Roaming = roamingList!![position]
            holder.amountTextView.setText(String.valueOf(roaming.rs))
            holder.validityTv.text = "Validity: " + roaming.validity
            holder.descriptionTv.setText(roaming.desc)
        }
    }

    override fun getItemCount(): Int {
        return if (roamingList != null) {
            roamingList!!.size
        } else {
            0
        }
    }

    fun setRoamingList(roamingList: List<Roaming>?) {
        this.roamingList = roamingList
        notifyDataSetChanged()
    }

    inner class RoamingListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView
        override fun onClick(v: View) {
            roamingItemClickListener.onRoamingItemClick(roamingList!![adapterPosition])
        }

        init {
            amountTextView = itemView.findViewById(R.id.plan_amount_textView)
            validityTv = itemView.findViewById(R.id.plan_validty_tv)
            descriptionTv = itemView.findViewById(R.id.plan_des_tv)
            itemView.setOnClickListener(this)
        }
    }

}