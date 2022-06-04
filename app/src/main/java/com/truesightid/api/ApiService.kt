package com.truesightid.api

import com.truesightid.data.source.remote.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/predict/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded"
    )
    fun getNewsPrediction(
        @Header("x-api-key") apiKey: String,
        @Field("content") content: String
    ): Call<NewsPredictionResponse>

    @FormUrlEncoded
    @POST("api/registration/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded"
    )
    fun postRegistrationForm(
        @Header("x-api-key") apiKey: String,
        @Field("username") username: String,
        @Field("full_name") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>


    @FormUrlEncoded
    @POST("api/auth/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded"
    )
    fun postLoginForm(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/search/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded"
    )
    fun getAllClaims(
        @Header("x-api-key") apiKey: String,
        @Field("keyword") keyword: String
    ): Call<ClaimsResponse>

    @FormUrlEncoded
    @POST("api/votes/up/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "x-api-key: nfuw4uh6zc52feq2ozyo7zg3oxs4apl0j6ii92oxbnyo0kro37kxf9c1cqfn3itp"
    )
    fun upvoteByClaimID(@Field("id") id: Int): Call<VoteResponse>

    @FormUrlEncoded
    @POST("api/votes/up/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "x-api-key: nfuw4uh6zc52feq2ozyo7zg3oxs4apl0j6ii92oxbnyo0kro37kxf9c1cqfn3itp"
    )
    fun downVoteByClaimID(@Field("id") id: Int): Call<VoteResponse>

    @FormUrlEncoded
    @POST("api/bookmarks/add/")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "x-api-key: nfuw4uh6zc52feq2ozyo7zg3oxs4apl0j6ii92oxbnyo0kro37kxf9c1cqfn3itp"
    )
    fun addBookmarkByClaimID(@Field("id") id: Int): Call<VoteResponse>

    @Multipart
    @POST("api/create/claim/")
    fun postClaimMultiPart(
        @Header("x-api-key") apiKey: String,
        @Part("title") title: String,
        @Part("description") description: String,
        @Part("fake") fake: Int,
        @Part("url") url: String,
        @Part attachment: MultipartBody.Part,
    ): Call<PostClaimResponse>
}