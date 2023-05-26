package com.example.mymv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActorResponse(
    @SerializedName("cast")
    val movieModels : List<ActorModel>
) : Parcelable {
    constructor() : this(mutableListOf())
}

