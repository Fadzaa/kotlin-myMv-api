package com.example.mymv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TVResponse(
    @SerializedName("results")
    val tvModels : MutableList<TVModel>,
) : Parcelable {
    constructor() : this(mutableListOf())
}
