package com.fullsekurity.urbandict.meanings

import androidx.databinding.ObservableField
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.urbandict.repository.storage.Meaning
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("UNCHECKED_CAST")
class MeaningsItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    val definition: ObservableField<String> = ObservableField("")
    val permalink: ObservableField<String> = ObservableField("")
    val thumbsUp: ObservableField<String> = ObservableField("")
    val author: ObservableField<String> = ObservableField("")
    val word: ObservableField<String> = ObservableField("")
    val defId: ObservableField<String> = ObservableField("")
    val currentVote: ObservableField<String> = ObservableField("")
    val writtenOn: ObservableField<String> = ObservableField("")
    val example: ObservableField<String> = ObservableField("")
    val thumbsDown: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        definition.set(item.definition)
        permalink.set(item.permalink)
        thumbsUp.set(item.thumbsUp.toString())
        author.set("By: ${item.author}")
        word.set(item.word)
        defId.set(item.defId.toString())
        currentVote.set(item.currentVote)
        val index = item.writtenOn.indexOf('T')
        writtenOn.set("On: ${item.writtenOn.substring(0, index)}")
        example.set(item.example)
        thumbsDown.set(item.thumbsDown.toString())
    }

}
