package com.fullsekurity.urbandict.repository.storage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meaning(

    @SerializedName(value = "name") var name: String,
    @SerializedName(value = "stargazers_count") var stargazersCount: Int,
    @SerializedName(value = "html_url") var htmlUrl: String

) : Parcelable