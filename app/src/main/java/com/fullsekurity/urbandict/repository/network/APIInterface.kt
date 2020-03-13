package com.fullsekurity.urbandict.repository.network

import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.utils.Constants
import com.fullsekurity.urbandict.utils.Constants.API_KEY_REQUEST_PARAM
import com.fullsekurity.urbandict.utils.Constants.LANGUAGE_REQUEST_PARAM
import com.fullsekurity.urbandict.utils.Constants.PAGE_REQUEST_PARAM
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
    ): Observable<List<Meaning>>
}