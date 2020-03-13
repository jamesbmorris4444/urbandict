package com.fullsekurity.urbandict.donateproducts

import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.utils.Utils

@Suppress("UNCHECKED_CAST")
class DonateProductsItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    private lateinit var donor: Meaning

    val voteCount: ObservableField<String> = ObservableField("")
    val video: ObservableField<String> = ObservableField("")
    val voteAverage: ObservableField<String> = ObservableField("")
    val lastName: ObservableField<String> = ObservableField("")
    val popularity: ObservableField<String> = ObservableField("")
    val firstName: ObservableField<String> = ObservableField("")
    val middleName: ObservableField<String> = ObservableField("")
    val branch: ObservableField<String> = ObservableField("")
    val aboRh: ObservableField<String> = ObservableField("")
    val gender: ObservableField<String> = ObservableField("")
    val overview: ObservableField<String> = ObservableField("")
    val dob: ObservableField<String> = ObservableField("")
    var inReassociate = false

    override fun setItem(item: Meaning) {
        donor = item
//        voteCount.set(item.voteCount.toString())
//        video.set(if (item.video) "T" else "F")
//        voteAverage.set(item.voteAverage.toString())
//        lastName.set(item.lastName)
//        popularity.set(item.popularity.toString())
//        firstName.set(item.firstName)
//        middleName.set(item.middleName)
//        branch.set(item.branch)
//        aboRh.set(item.aboRh)
//        gender.set(if (item.gender) "Male" else "Female")
//        overview.set(item.overview)
//        dob.set(item.dob)
//        this.inReassociate = item.inReassociate
    }

}
