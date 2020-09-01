package com.pdma.pdma.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.Frc
import java.lang.String


class FrcListAdapter(private val onFrcListItemClickListener: OnFrcListItemClickListener)
    :RecyclerView.Adapter<FrcListAdapter.FrcListViewHolder>(){

    private var frcList:List<Frc> = ArrayList<Frc>()

    interface OnFrcListItemClickListener{
        fun onFrcItemClick(frc: Frc)
    }

    inner class FrcListViewHolder(itemView:View):
            RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var validityTv: TextView
        var descriptionTv: TextView
        var amountTextView: TextView

        override fun onClick(v: View?) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrcListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.plan_list_item, parent, false)
        return FrcListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return frcList.size
    }

    fun setFrcList(frcList:List<Frc>){
        this.frcList = frcList
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: FrcListViewHolder, position: Int) {

        frcList?.let {
            val frc = frcList[position]
            holder.amountTextView.text = String.valueOf(frc.rs)
            holder.validityTv.text = "Validity: " + frc.validity
            holder.descriptionTv.setText(frc.desc)
        }
    }
}