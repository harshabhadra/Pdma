package com.pdma.pdma.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.databinding.CategoryListItemBinding
import com.pdma.pdma.domain.CategoryItem

class HomeCategoryAdapter(private val clickListener: HomeCategoryClickListener):
ListAdapter<CategoryItem,HomeCategoryAdapter.HomeCategoryViewHolder>(HomeCategoryDiffUtilCallback()){

    class HomeCategoryViewHolder(private val binding:CategoryListItemBinding)
        :RecyclerView.ViewHolder(binding.root){

        fun bind(item:CategoryItem, clickListener: HomeCategoryClickListener){
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent:ViewGroup):HomeCategoryViewHolder{

                val binding = CategoryListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HomeCategoryViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewHolder {
        return HomeCategoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {

        val item = getItem(position)
        item?.let {
            holder.bind(item,clickListener)
        }
    }
}

class HomeCategoryClickListener(val clickListener:(catItem:CategoryItem)->Unit){
    fun onCategoryClick(catItem: CategoryItem) = clickListener(catItem)
}

class HomeCategoryDiffUtilCallback: DiffUtil.ItemCallback<CategoryItem>(){
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return  oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return  oldItem.catName == newItem.catName
    }
}