package com.fullsekurity.urbandict.repository

import android.net.Network
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.repository.network.APIClient
import com.fullsekurity.urbandict.repository.network.APIInterface
import com.fullsekurity.urbandict.repository.storage.Meaning
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class Repository(private val callbacks: Callbacks) {

    private val tag = Repository::class.java.simpleName
    private val donorsService: APIInterface = APIClient.client
    private var isMetered: Boolean = false
    private var cellularNetwork: Network? = null
    private var wiFiNetwork: Network? = null
    var isOfflineMode = true
    val liveViewMeaningList: MutableLiveData<List<Meaning>> = MutableLiveData()
    var newMeaning: Meaning? = null
    var newMeaningInProgress = false

    fun getUrbanDictionaryMeanings(progressBar: ProgressBar) {
        var disposable: Disposable? = null
        disposable = donorsService.getMeanings("strong")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ donorResponse ->
                disposable?.dispose()
                progressBar.visibility = View.GONE
                LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RESPONSE   %s", donorResponse[0].definition))
            },
            { throwable ->
                disposable?.dispose()
                progressBar.visibility = View.GONE
                LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.EXC), "getUrbanDictionaryMeanings", throwable)
            })
    }

}