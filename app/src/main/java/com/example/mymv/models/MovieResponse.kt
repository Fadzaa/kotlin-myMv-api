package com.example.mymv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponse(
    @SerializedName("results")
    val movieModels : MutableList<MovieModel>,
) : Parcelable {
    constructor() : this(mutableListOf())
}

