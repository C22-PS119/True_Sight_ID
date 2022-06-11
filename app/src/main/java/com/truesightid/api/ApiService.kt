package com.truesightid.api

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
        @Field("username") username: String,
        @Field("full_name") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegistrationResponse>


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
    ): Call<GetClaimsResponse>

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
    @POST("api/delete/claim/")
    fun deleteByClaimID(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<AddRemoveResponse>

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
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("fake") fake: Int,
        @Part("url") url: RequestBody,
        @Part attachment: List<MultipartBody.Part>?,
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

    @FormUrlEncoded
    @POST("/api/get/myclaims/")
    fun getMyClaims(
        @Header("x-api-key") apiKey: String,
        @Field("start") start: Int = 0
    ): Call<MyClaimResponse>

    @FormUrlEncoded
    @POST("/api/bookmarks/list/")
    fun getMyBookmark(
        @Header("x-api-key") apiKey: String,
        @Field("start") start: Int = 0
    ): Call<MyBookmarkResponse>

    @FormUrlEncoded
    @POST("/api/bookmarks/add/")
    fun addBookmarkByClaimId(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<AddRemoveResponse>

    @FormUrlEncoded
    @POST("/api/bookmarks/remove/")
    fun removeBookmarkByClaimId(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<AddRemoveResponse>

    @FormUrlEncoded
    @POST("/api/set/password/")
    fun changePassword(
        @Header("x-api-key") apiKey: String,
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String
    ): Call<SetPasswordResponse>

    @FormUrlEncoded
    @POST("/api/auth/reset/")
    fun sendEmailVerification(
        @Field("email") email: String
    ): Call<EmailVerificationRespond>

    @FormUrlEncoded
    @POST("/api/auth/confirm/")
    fun confirmVerificationCode(
        @Field("user_id") user_id: Int,
        @Field("verification_code") verification_code: String
    ): Call<ConfirmVerificationRespond>

    @FormUrlEncoded
    @POST("/api/set/password/")
    fun resetPassword(
        @Field("reset_key") reset_key: String,
        @Field("new_password") new_password: String
    ): Call<SetPasswordResponse>

    @FormUrlEncoded
    @POST("/api/create/comments/")
    fun addComment(
        @Header("x-api-key") apiKey: String,
        @Field("claim_id") id: Int,
        @Field("text") comment: String
    ): Call<AddRemoveResponse>

    @FormUrlEncoded
    @POST("/api/get/comments/")
    fun getComments(
        @Header("x-api-key") apiKey: String,
        @Field("claim_id") id: Int
    ): Call<GetCommentsResponse>

    @FormUrlEncoded
    @POST("/api/get/comments/")
    fun deleteComment(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<AddRemoveResponse>

    @FormUrlEncoded
    @POST("/api/get/comments/")
    fun deleteClaim(
        @Header("x-api-key") apiKey: String,
        @Field("id") id: Int
    ): Call<AddRemoveResponse>

    @FormUrlEncoded
    @POST("api/search/")
    fun getClaimsBySearch(
        @Header("x-api-key") apiKey: String,
        @Field("keyword") keyword: String
    ): Call<ClaimsBySearchResponse>


}