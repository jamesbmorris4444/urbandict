package com.fullsekurity.urbandict.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase.deleteDatabase
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.activity.MainActivity
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.logger.LogUtils.TagFilter.EXC
import com.fullsekurity.urbandict.modal.StandardModal
import com.fullsekurity.urbandict.repository.network.APIClient
import com.fullsekurity.urbandict.repository.network.APIInterface
import com.fullsekurity.urbandict.repository.storage.BloodDatabase
import com.fullsekurity.urbandict.repository.storage.Donor
import com.fullsekurity.urbandict.repository.storage.DonorWithProducts
import com.fullsekurity.urbandict.repository.storage.Product
import com.fullsekurity.urbandict.utils.Constants
import com.fullsekurity.urbandict.utils.Constants.MAIN_DATABASE_NAME
import com.fullsekurity.urbandict.utils.Constants.MODIFIED_DATABASE_NAME
import com.fullsekurity.urbandict.utils.Utils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit


class Repository(private val callbacks: Callbacks) {

    private val tag = Repository::class.java.simpleName
    lateinit var mainBloodDatabase: BloodDatabase
    lateinit var stagingBloodDatabase: BloodDatabase
    private val donorsService: APIInterface = APIClient.client
    private var isMetered: Boolean = false
    private var cellularNetwork: Network? = null
    private var wiFiNetwork: Network? = null
    var isOfflineMode = true
    val liveViewDonorList: MutableLiveData<List<Donor>> = MutableLiveData()
    var newDonor: Donor? = null
    var newDonorInProgress = false

    fun setBloodDatabase(context: Context) {
        val dbList = BloodDatabase.newInstance(context, MAIN_DATABASE_NAME, MODIFIED_DATABASE_NAME)
        mainBloodDatabase = dbList[0]
        stagingBloodDatabase = dbList[1]
    }

    // The code below here refreshes the main donations base

    fun refreshDatabase(progressBar: ProgressBar, activity: MainActivity) {

        var disposable: Disposable? = null
        disposable = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, 13)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ donorResponse ->
                disposable?.dispose()
                initializeDataBase(progressBar, donorResponse.results, donorResponse.products, activity)
            },
            { throwable ->
                progressBar.visibility = View.GONE
                disposable?.dispose()

            })
    }

    private fun initializeDataBase(progressBar: ProgressBar, donors: List<Donor>, products: List<List<Product>>, activity: MainActivity) {
        for (donorIndex in donors.indices) {
            for (productIndex in products[donorIndex].indices) {
                products[donorIndex][productIndex].donorId = donors[donorIndex].id
            }
        }
    }

}