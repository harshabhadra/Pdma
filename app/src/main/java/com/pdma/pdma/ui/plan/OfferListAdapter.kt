package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.Rdata
import java.lang.String
import java.util.*

class OfferListAdapter(private val onOfferListItemClickListener: OnOfferListItemClickListener) :
    RecyclerView.Adapter<OfferListAdapter.OfferListViewHolder>() {
    private var offerList: List<Rdata>? = ArrayList<Rdata>()

    interface OnOfferListItemClickListener {
        fun onOfferItemClick(rdata: Rdata?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferListViewHolder {
        return OfferListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: OfferListViewHolder,
        position: Int
    ) {
        if (offerList != null) {
            val dataPlan: Rdata = offerList!![position]
            holder.amountTextView.setText(String.valueOf(dataPlan.price))
            holder.validityTv.text = "Validity: "
            holder.descriptionTv.setText(dataPlan.logdesc)
        }
    }

    override fun getItemCount(): Int {
        return offerList!!.size
    }

    fun setOfferList(offerList: List<Rdata>?) {
        this.offerList = offerList
        notifyDataSetChanged()
    }

    inner class OfferListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView
        override fun onClick(v: View) {
            onOfferListItemClickListener.onOfferItemClick(offerList!![adapterPosition])
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