package com.fullsekurity.urbandict.activity

import android.view.View
import com.fullsekurity.urbandict.activity.MainActivity
import com.fullsekurity.urbandict.donateproducts.DonateProductsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchDonateProductsListViewModel() : DonateProductsListViewModel?
}