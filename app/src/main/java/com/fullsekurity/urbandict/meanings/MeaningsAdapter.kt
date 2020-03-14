package com.fullsekurity.urbandict.meanings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.databinding.MeaningsListItemBinding
import com.fullsekurity.urbandict.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.ui.UIViewModel


class MeaningsAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Meaning, MeaningsItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null
    lateinit var uiViewModel: UIViewModel

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningsViewHolder {
        val meaningsListItemBinding: MeaningsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.meanings_list_item, parent, false)
        val meaningsItemViewModel = ViewModelProvider(callbacks.fetchFragment() as MeaningsFragment, MeaningsItemViewModelFactory(callbacks)).get(MeaningsItemViewModel::class.java)
        meaningsListItemBinding.meaningsItemViewModel = meaningsItemViewModel
        meaningsListItemBinding.uiViewModel = uiViewModel
        return MeaningsViewHolder(meaningsListItemBinding.root, meaningsItemViewModel, meaningsListItemBinding)
    }

    inner class MeaningsViewHolder internal constructor(itemView: View, viewModel: MeaningsItemViewModel, viewDataBinding: MeaningsListItemBinding) :
        ItemViewHolder<Meaning, MeaningsItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Meaning, MeaningsItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

    override fun itemFilterable(item: Meaning, constraint: String): Boolean {
        return true
    }

}