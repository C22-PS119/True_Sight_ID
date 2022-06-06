package com.truesightid.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.truesightid.api.ApiHelper
import com.truesightid.data.source.remote.request.*
import com.truesightid.data.source.remote.response.*

class RemoteDataSource private constructor(private val apiHelper: ApiHelper) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: ApiHelper): RemoteDataSource = instance ?: synchronized(this) {
            RemoteDataSource(helper).apply { instance = this }
        }
    }

    fun loginRequest(request: LoginRequest): LiveData<ApiResponse<LoginResponse>> {
        val resultLogin = MutableLiveData<ApiResponse<LoginResponse>>()
        apiHelper.loginRequest(request, object : LoginRequestCallback {
            override fun onLoginRequestResponse(loginResponse: LoginResponse) {
                if (loginResponse.status == "success")
                    resultLogin.value = ApiResponse.success(loginResponse)
                else
                    resultLogin.value = ApiResponse.error(loginResponse.message ?: "Failed to GET message", LoginResponse())
            }
        })
        return resultLogin
    }

    fun registrationRequest(
        request: RegistrationRequest
    ): LiveData<ApiResponse<RegistrationResponse>> {
        val resultRegister = MutableLiveData<ApiResponse<RegistrationResponse>>()
        apiHelper.registrationRequest(request,
            object : RegistrationRequestCallback {
                override fun onRegistrationRequestResponse(registerResponse: RegistrationResponse) {
                    resultRegister.value = ApiResponse.success(registerResponse)
                }
            })
        return resultRegister
    }

    fun getAllClaims(request: ClaimRequest): LiveData<ApiResponse<ClaimsResponse>> {
        val resultClaims = MutableLiveData<ApiResponse<ClaimsResponse>>()
        apiHelper.getClaimsRequest(request, object : ClaimsRequestCallback {
            override fun onClaimsRequestResponse(claimsResponse: ClaimsResponse) {
                resultClaims.value = ApiResponse.success(claimsResponse)
            }
        })
        return resultClaims
    }

    fun postClaimRequest(request: PostClaimRequest): LiveData<ApiResponse<PostClaimResponse>> {
        val resultPost = MutableLiveData<ApiResponse<PostClaimResponse>>()
        apiHelper.postResponse(request, object : PostClaimRequestCallback {
            override fun onPostClaimRequestResponse(postClaimResponse: PostClaimResponse) {
                resultPost.value = ApiResponse.success(postClaimResponse)
            }

        })
        return resultPost
    }

    fun postProfileWithAvatarRequest(request: EditProfileWithAvatarRequest): LiveData<ApiResponse<PostProfileResponse>> {
        val resultPost = MutableLiveData<ApiResponse<PostProfileResponse>>()
        apiHelper.postProfileWithAvatarResponse(request, object : PostProfileRequestCallback {
            override fun onPostProfileRequestResponse(postProfileResponse: PostProfileResponse) {
                resultPost.value = ApiResponse.success(postProfileResponse)
            }

        })
        return resultPost
    }

    fun postProfileRequest(request: EditProfileRequest): LiveData<ApiResponse<PostProfileResponse>> {
        val resultPost = MutableLiveData<ApiResponse<PostProfileResponse>>()
        apiHelper.postProfileResponse(request, object : PostProfileRequestCallback {
            override fun onPostProfileRequestResponse(postProfileResponse: PostProfileResponse) {
                resultPost.value = ApiResponse.success(postProfileResponse)
            }

        })
        return resultPost
    }

    fun getUserProfileRequest(request: GetProfileRequest): LiveData<ApiResponse<UserResponse>> {
        val resultPost = MutableLiveData<ApiResponse<UserResponse>>()
        apiHelper.getUserProfileResponse(request, object : GetProfileRequestCallback {
            override fun onGetUserProfileRequestResponse(userProfileResponse: UserResponse) {
                resultPost.value = ApiResponse.success(userProfileResponse)
            }

        })
        return resultPost
    }
    fun upVoteRequestById(api_key:String, id: Int) {
        apiHelper.voteByClaimIdRequest(true, api_key, id)
    }

    fun downVoteRequestById(api_key:String, id: Int) {
        apiHelper.voteByClaimIdRequest(false,api_key, id)
    }

    interface ClaimsRequestCallback {
        fun onClaimsRequestResponse(claimsResponse: ClaimsResponse)
    }

    interface LoginRequestCallback {
        fun onLoginRequestResponse(loginResponse: LoginResponse)
    }

    interface RegistrationRequestCallback {
        fun onRegistrationRequestResponse(registerResponse: RegistrationResponse)
    }

    interface PostClaimRequestCallback {
        fun onPostClaimRequestResponse(postClaimResponse: PostClaimResponse)
    }

    interface PostProfileRequestCallback {
        fun onPostProfileRequestResponse(postProfileResponse: PostProfileResponse)
    }

    interface GetProfileRequestCallback {
        fun onGetUserProfileRequestResponse(userProfileResponse: UserResponse)
    }
}