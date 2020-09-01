package com.pdma.pdma.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryItem(
    val catImg:Int,
    val catName:String
):Parcelable