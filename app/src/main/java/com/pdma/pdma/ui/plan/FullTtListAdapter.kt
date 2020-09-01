package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.FullTt
import java.lang.String
import java.util.*

class FullTtListAdapter(private val fullttItemClickListener: OnFullttItemClickListener) :
    RecyclerView.Adapter<FullTtListAdapter.FullTtViewHolder>() {
    private var fullTtList: List<FullTt>? = ArrayList<FullTt>()

    interface OnFullttItemClickListener {
        fun onFullttItemClick(fullTt: FullTt?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FullTtViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        return FullTtViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FullTtViewHolder,
        position: Int
    ) {
        if (fullTtList != null) {
            val fullTt: FullTt = fullTtList!![position]
            holder.amountTextView.setText(String.valueOf(fullTt.rs))
            holder.validityTv.text = "Validity: " + fullTt.validity
            holder.descriptionTv.setText(fullTt.desc)
        }
    }

    override fun getItemCount(): Int {
        return if (fullTtList != null) {
            fullTtList!!.size
        } else {
            0
        }
    }

    fun setFullTtList(fullTtList: List<FullTt>?) {
        this.fullTtList = fullTtList
        notifyDataSetChanged()
    }

    inner class FullTtViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView
        override fun onClick(v: View) {
            fullttItemClickListener.onFullttItemClick(fullTtList!![adapterPosition])
        }

        init {
            amountTextView = itemView.findViewById(R.id.plan_amount_textView)
            validityTv = itemView.findViewById(R.id.plan_validty_tv)
            descriptionTv = itemView.findViewById(R.id.plan_des_tv)
            itemView.setOnClickListener(this)
        }
    }

}