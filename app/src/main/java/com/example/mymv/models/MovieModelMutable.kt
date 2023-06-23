package com.example.mymv.models



import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MovieModelMutable(
    @SerializedName("id")
    val id : String?,

    @SerializedName("title")
    val title : String?,

    @SerializedName("release_date")
    val release : String?,

    @SerializedName("poster_path")
    val poster : String?,

    @SerializedName("overview")
    val overview : String?,

    @SerializedName("backdrop_path")
    val backdropPath : String?,

    @SerializedName("runtime")
    val runtime: String?,

    @SerializedName("vote_average")
    val voteAverage:String?,

    @SerializedName("vote_count")
    val voteCount: String?,

    @SerializedName("genres")
    val genres: ArrayList<Genre>?




) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "", "", ArrayList())
}

