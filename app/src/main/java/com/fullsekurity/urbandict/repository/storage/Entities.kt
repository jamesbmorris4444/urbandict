package com.fullsekurity.urbandict.repository.storage

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Meaning(

    @SerializedName(value = "definition") var definition: String = "",
    @SerializedName(value = "permalink") var permalink: String = "",
    @SerializedName(value = "thumbs_up") var thumbsUp: Int = 0,
    @SerializedName(value = "sound_urls") var soundUrls: List<String> = mutableListOf(),
    @SerializedName(value = "author") var author: String = "",
    @SerializedName(value = "word") var word: String = "",
    @SerializedName(value = "defid") var defId: Int = 0,
    @SerializedName(value = "current_vote") var currentVote: String = "",
    @SerializedName(value = "written_on") var writtenOn: String = "",
    @SerializedName(value = "example") var example: String = "",
    @SerializedName(value = "thumbs_down") var thumbsDown: Int = 0

) : Serializable