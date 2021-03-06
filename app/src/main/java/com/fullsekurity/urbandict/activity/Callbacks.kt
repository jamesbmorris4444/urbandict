package com.fullsekurity.urbandict.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.fullsekurity.urbandict.meanings.MeaningsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchmeaningsListViewModel() : MeaningsListViewModel?
}