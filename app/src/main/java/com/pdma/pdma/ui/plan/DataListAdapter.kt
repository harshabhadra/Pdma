package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.DataPlan
import java.lang.String
import java.util.*

class DataListAdapter(private val dataItemClickListener: OnDataItemClickListener) :
    RecyclerView.Adapter<DataListAdapter.DataListViewHolder>() {

    private var dataPlanList: List<DataPlan>? = ArrayList<DataPlan>()

    interface OnDataItemClickListener {
        fun onDataItemClick(dataPlan: DataPlan?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.plan_list_item, parent, false)
        return DataListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DataListViewHolder,
        position: Int
    ) {
        if (dataPlanList != null) {
            val dataPlan: DataPlan = dataPlanList!![position]
            holder.amountTextView.text = String.valueOf(dataPlan.rs)
            holder.validityTv.text = "Validity: " + dataPlan.validity
            holder.descriptionTv.setText(dataPlan.desc)
        }
    }

    override fun getItemCount(): Int {
        return if (dataPlanList != null) {
            dataPlanList!!.size
        } else {
            0
        }
    }

    fun setDataPlanList(dataPlanList: List<DataPlan>?) {
        this.dataPlanList = dataPlanList
        notifyDataSetChanged()
    }

    inner class DataListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView

        override fun onClick(v: View) {
            dataItemClickListener.onDataItemClick(dataPlanList!![adapterPosition])
        }

        init {
            amountTextView = itemView.findViewById(R.id.plan_amount_textView)
            validityTv = itemView.findViewById(R.id.plan_validty_tv)
            descriptionTv = itemView.findViewById(R.id.plan_des_tv)
            itemView.setOnClickListener { v: View ->
                onClick(
                    v
                )
            }
        }
    }

}