package com.truesightid.api

import com.truesightid.data.source.remote.request.EditProfileRequest
import com.truesightid.data.source.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/predict/")
    fun getNewsPrediction(
        @Header("x-api-key") apiKey: String,
        @Field("content") content: String
    ): Call<NewsPredictionResponse>

    @FormUrlEncoded
    @POST("api/registration/")
    fun postRegistrationForm(
        @Header("x-api-key") apiKey: String,
        @Field("username") username: String,
        @Field("full_name") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>


    @FormUrlEncoded
    @POST("api/auth/")
    fun postLoginForm(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/search/")
    fun getAllClaims(
        @Header("x-api-key") apiKey: String,
        @Field("keyword") keyword: String
    ): Call<ClaimsResponse>

    @FormUrlEncoded
    @POST("api/votes/up/")
    fun upvoteByClaimID(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<VoteResponse>

    @FormUrlEncoded
    @POST("api/votes/down/")
    fun downVoteByClaimID(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<VoteResponse>

    @FormUrlEncoded
    @POST("api/bookmarks/add/")
    fun addBookmarkByClaimID(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<VoteResponse>

    @FormUrlEncoded
    @POST("api/get/profile/")
    fun getProfileByID(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<UserResponse>

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

    @Multipart
    @POST("api/set/profile/")
    fun setProfileWithAvatar(
        @Header("x-api-key") apiKey: String,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part avatar: MultipartBody.Part?,
    ): Call<PostProfileResponse>

    @Multipart
    @POST("api/set/profile/")
    fun setProfile(
        @Header("x-api-key") apiKey: String,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
    ): Call<PostProfileResponse>
}