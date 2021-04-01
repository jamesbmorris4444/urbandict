package com.fullsekurity.urbandict.repository.network

import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.utils.Constants
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {
    @GET("users/{user}/repos")
    fun getMeanings(
        @Path(Constants.URBANDICT_TERM) term: String,
        @Query(Constants.PER_PAGE) perPage: Int,
        @Query(Constants.PAGE) page: Int,
        @Query(Constants.DIRECTION) direction: String
    ): Flowable<List<Meaning>>
}

data class MeaningsResponse (
    val list: List<Meaning>
)