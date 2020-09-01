package com.pdma.pdma.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("categoryImg")
fun setCategoryImage(iv:ImageView, img:Int){
    Glide.with(iv.context)
        .load(img)
        .into(iv)
}