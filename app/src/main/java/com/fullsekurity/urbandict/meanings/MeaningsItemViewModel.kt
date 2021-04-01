package com.fullsekurity.urbandict.meanings

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.webview.QuizWebView

class MeaningsItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    val name: ObservableField<String> = ObservableField("")
    val stargazersCount: ObservableField<String> = ObservableField("")
    val htmlUrl: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        name.set("Name: ${item.name}")
        stargazersCount.set("Stars: ${item.stargazersCount}")
        htmlUrl.set(item.htmlUrl)
    }

    fun onUrlClicked(view: View) {
        htmlUrl.get()?.let {
            callbacks.fetchmeaningsListViewModel().onUrlClicked(view, it)
        }
    }

}
