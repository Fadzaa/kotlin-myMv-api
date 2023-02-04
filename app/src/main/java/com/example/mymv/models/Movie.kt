package com.example.mymv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id")
    val id : String?,

    @SerializedName("title")
    val title : String?,

    @SerializedName("release_date")
    val release : String?,

    @SerializedName("poster_path")
    val poster : String?,

    @SerializedName("name")
    val name : String?,

    @SerializedName("overview")
    val overview : String?,

    @SerializedName("backdrop_path")
    val backdropPath : String?,


) : Parcelable {
    constructor() : this("","","","", "","", "")
}