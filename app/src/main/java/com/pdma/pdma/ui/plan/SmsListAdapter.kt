package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.Sms
import java.lang.String
import java.util.*

class SmsListAdapter(private val smsItemClickListener: OnSmsItemClickListener) :
    RecyclerView.Adapter<SmsListAdapter.SmsListViewHolder>() {
    private var smsList: List<Sms>? = ArrayList<Sms>()

    interface OnSmsItemClickListener {
        fun onSmsItemClick(sms: Sms?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmsListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.plan_list_item, parent, false)
        return SmsListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SmsListViewHolder,
        position: Int
    ) {
        if (smsList != null) {
            val sms: Sms = smsList!![position]
            holder.amountTextView.setText(String.valueOf(sms.rs))
            holder.validityTv.text = "Validity: " + sms.validity
            holder.descriptionTv.setText(sms.desc)
        }
    }

    override fun getItemCount(): Int {
        return if (smsList != null) {
            smsList!!.size
        } else {
            0
        }
    }

    fun setSmsList(smsList: List<Sms>?) {
        this.smsList = smsList
        notifyDataSetChanged()
    }

    inner class SmsListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView
        override fun onClick(v: View) {
            smsItemClickListener.onSmsItemClick(smsList!![adapterPosition])
        }

        init {
            amountTextView = itemView.findViewById(R.id.plan_amount_textView)
            validityTv = itemView.findViewById(R.id.plan_validty_tv)
            descriptionTv = itemView.findViewById(R.id.plan_des_tv)
            itemView.setOnClickListener(this)
        }
    }

}