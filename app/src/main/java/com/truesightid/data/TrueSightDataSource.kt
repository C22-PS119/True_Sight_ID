package com.truesightid.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.request.ClaimRequest
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.request.PostClaimRequest
import com.truesightid.data.source.remote.request.RegistrationRequest
import com.truesightid.data.source.remote.response.LoginResponse
import com.truesightid.data.source.remote.response.PostClaimResponse
import com.truesightid.data.source.remote.response.RegistrationResponse
import com.truesightid.utils.Resource

interface TrueSightDataSource {
    fun getAllClaims(request: ClaimRequest): LiveData<Resource<PagedList<ClaimEntity>>>

    fun upVoteClaimById(api_key:String, id: Int)

    fun downVoteClaimById(api_key:String, id: Int)

    fun loginRequest(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>>

    fun postClaim(postClaimRequest: PostClaimRequest): LiveData<ApiResponse<PostClaimResponse>>

    fun postProfileWithAvatar(editProfileWithAvatarRequest: EditProfileWithAvatarRequest): LiveData<ApiResponse<PostProfileResponse>>

    fun postProfile(editProfileRequest: EditProfileRequest): LiveData<ApiResponse<PostProfileResponse>>

    fun getUserProfile(editProfileRequest: GetProfileRequest): LiveData<ApiResponse<UserResponse>>

    fun deleteLocalClaims()

    fun registrationRequest(registrationRequest: RegistrationRequest): LiveData<ApiResponse<RegistrationResponse>>
}