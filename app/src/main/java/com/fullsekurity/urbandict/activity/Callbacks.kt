package com.fullsekurity.urbandict.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.fullsekurity.urbandict.meanings.MeaningsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchFragment(): Fragment?
    fun fetchRootView() : View
    fun fetchDonateProductsListViewModel() : MeaningsListViewModel?
}