package com.example.mymv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActorModel(
    @SerializedName("id")
    val id : String?,

    @SerializedName("name")
    val actorName : String?,

    @SerializedName("profile_path")
    val actorImage : String?,

    @SerializedName("birthday")
    val actorBirthday : String?,

    @SerializedName("biography")
    val actorBiography : String?,

    @SerializedName("gender")
    val actorGender : String?,

    @SerializedName("place_of_birth")
    val actorPlaceBirth : String?,

    @SerializedName("poster_path")
    val posterCredit: String?,





) : Parcelable {
    constructor() : this("","","","","","","","")
}

@Parcelize
data class CastResponseModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val castList: List<ActorModel>
): Parcelable {
    constructor() : this(0,mutableListOf())
}

