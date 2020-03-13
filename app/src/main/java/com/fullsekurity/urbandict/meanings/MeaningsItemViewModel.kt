package com.fullsekurity.urbandict.meanings

import androidx.databinding.ObservableField
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.urbandict.repository.storage.Meaning

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
        author.set(item.author)
        word.set(item.word)
        defId.set(item.defId.toString())
        currentVote.set(item.currentVote)
        writtenOn.set(item.writtenOn)
        example.set(item.example)
        thumbsDown.set(item.thumbsDown.toString())
    }

}
