package com.fullsekurity.urbandict.repository.network

import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.utils.Constants
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIInterface {
    @Headers(
        Constants.URBANDICT_RAPID_API_HOST,
        Constants.URBANDICT_RAPID_API_KEY
    )
    @GET("define")
    fun getMeanings(
        @Query(Constants.URBANDICT_TERM) term: String
    ): Flowable<MeaningsResponse>
}

data class MeaningsResponse (
    val list: List<Meaning>
)