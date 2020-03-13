package com.fullsekurity.urbandict.repository.storage

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Meaning(

    @SerializedName(value = "definition") var definition: String = "",
    @ColumnInfo(name = "video") @SerializedName(value = "video") var video: Boolean = false,
    @ColumnInfo(name = "vote_average") @SerializedName(value = "vote_average") var voteAverage: Float = 0f,
    @ColumnInfo(name = "title") @SerializedName(value = "title") var lastName: String = "",
    @ColumnInfo(name = "popularity") @SerializedName(value = "popularity") var popularity: Float = 0f,
    @ColumnInfo(name = "poster_path") @SerializedName(value = "poster_path") var firstName: String = "",
    @ColumnInfo(name = "original_language") @SerializedName(value = "original_language") var middleName: String = "",
    @ColumnInfo(name = "original_title") @SerializedName(value = "original_title") var branch: String = "",
    @ColumnInfo(name = "backdrop_path") @SerializedName(value = "backdrop_path") var aboRh: String = "",
    @ColumnInfo(name = "adult") @SerializedName(value = "adult") var gender: Boolean = false,
    @ColumnInfo(name = "overview") @SerializedName(value = "overview") var overview: String = "",
    @ColumnInfo(name = "release_date") @SerializedName(value = "release_date") var dob: String = "",
    @Ignore var inReassociate: Boolean = false

) : Serializable