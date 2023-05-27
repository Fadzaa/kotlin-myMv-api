package com.example.mymv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GenreModel(
    @SerializedName("id")
    val id : String?,

    @SerializedName("name")
    val name : String?,

) : Parcelable {
    constructor() : this("","")
}

@Parcelize
data class GenreResponse(
    @SerializedName("genres")
    val genreModels : List<GenreModel>
) : Parcelable {
    constructor() : this(mutableListOf())
}