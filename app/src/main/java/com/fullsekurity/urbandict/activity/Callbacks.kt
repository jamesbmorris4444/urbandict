package com.fullsekurity.urbandict.activity

import android.view.View
import com.fullsekurity.urbandict.meanings.DonateProductsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchDonateProductsListViewModel() : DonateProductsListViewModel?
}