package com.fullsekurity.urbandict.donateproducts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.databinding.DonateProductsListItemBinding
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.Utils

class DonateProductsAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Meaning, DonateProductsItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null
    lateinit var uiViewModel: UIViewModel

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningsViewHolder {
        val donateProductsListItemBinding: DonateProductsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.donate_products_list_item, parent, false)
        val donateProductsItemViewModel = DonateProductsItemViewModel(callbacks)
        donateProductsListItemBinding.donateProductsItemViewModel = donateProductsItemViewModel
        donateProductsListItemBinding.uiViewModel = uiViewModel
        return MeaningsViewHolder(donateProductsListItemBinding.root, donateProductsItemViewModel, donateProductsListItemBinding)
    }

    inner class MeaningsViewHolder internal constructor(itemView: View, viewModel: DonateProductsItemViewModel, viewDataBinding: DonateProductsListItemBinding) :
        ItemViewHolder<Meaning, DonateProductsItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Meaning, DonateProductsItemViewModel>, position: Int) {
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