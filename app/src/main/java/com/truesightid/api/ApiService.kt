package com.truesightid.api

import com.truesightid.data.source.remote.response.AddClaimResponse
import com.truesightid.data.source.remote.response.NewsPredictionResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/predict/")
    @Headers(
        "x-api-key: nfuw4uh6zc52feq2ozyo7zg3oxs4apl0j6ii92oxbnyo0kro37kxf9c1cqfn3itp"
    )
    fun getNewsPrediction(@Field("content") content: String): Call<NewsPredictionResponse>

    @Multipart
    @POST("api/create/claim/")
    @Headers(
        "x-api-key: nfuw4uh6zc52feq2ozyo7zg3oxs4apl0j6ii92oxbnyo0kro37kxf9c1cqfn3itp"
    )
    fun addClaim(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("fake") fake: RequestBody,
        @Part("url") url: RequestBody
    ): Call<AddClaimResponse>
}