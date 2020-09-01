package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.Stv
import java.lang.String
import java.util.*

class StvListAdapter(private val stvItemClickListener: OnStvItemClickListener) :
    RecyclerView.Adapter<StvListAdapter.StvListViewHolder>() {
    private var stvList: List<Stv>? = ArrayList<Stv>()

    interface OnStvItemClickListener {
        fun onStvItemClick(stv: Stv?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StvListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        return StvListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StvListViewHolder,
        position: Int
    ) {
        if (stvList != null) {
            val stv: Stv = stvList!![position]
            holder.amountTextView.setText(String.valueOf(stv.rs))
            holder.validityTv.text = "Validity: " + stv.validity
            holder.descriptionTv.setText(stv.desc)
        }
    }

    override fun getItemCount(): Int {
        return if (stvList != null) {
            stvList!!.size
        } else {
            0
        }
    }

    fun setStvList(stvList: List<Stv>?) {
        this.stvList = stvList
        notifyDataSetChanged()
    }

    inner class StvListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView
        override fun onClick(v: View) {
            stvItemClickListener.onStvItemClick(stvList!![adapterPosition])
        }

        init {
            amountTextView = itemView.findViewById(R.id.plan_amount_textView)
            validityTv = itemView.findViewById(R.id.plan_validty_tv)
            descriptionTv = itemView.findViewById(R.id.plan_des_tv)
            itemView.setOnClickListener(this)
        }
    }

}