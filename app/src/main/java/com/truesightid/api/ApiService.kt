package com.truesightid.api

import com.truesightid.data.source.remote.response.NewsPredictionResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("api/predict/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "x-api-key: invmj96ng4u3blkt7a145kk3wkqxgb7bnzt59quq1d4m2575b46koto3vwfu25hf"
    )
    fun getNewsPrediction(@Field("content") content: String): Call<NewsPredictionResponse>
}