package com.truesightid.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.truesightid.api.ApiHelper
import com.truesightid.data.source.remote.request.ClaimRequest
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.request.PostClaimRequest
import com.truesightid.data.source.remote.request.RegistrationRequest
import com.truesightid.data.source.remote.response.ClaimsResponse
import com.truesightid.data.source.remote.response.LoginResponse
import com.truesightid.data.source.remote.response.PostClaimResponse
import com.truesightid.data.source.remote.response.RegisterResponse

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
                resultLogin.value = ApiResponse.success(loginResponse)
            }
        })
        return resultLogin
    }

    fun registrationRequest(
        request: RegistrationRequest
    ): LiveData<ApiResponse<RegisterResponse>> {
        val resultRegister = MutableLiveData<ApiResponse<RegisterResponse>>()
        apiHelper.registrationRequest(request,
            object : RegistrationRequestCallback {
                override fun onRegistrationRequestResponse(registerResponse: RegisterResponse) {
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

    fun upVoteRequestById(id: Int) {
        apiHelper.voteByClaimIdRequest(true, id)
    }

    fun downVoteRequestById(id: Int) {
        apiHelper.voteByClaimIdRequest(false, id)
    }

    interface ClaimsRequestCallback {
        fun onClaimsRequestResponse(claimsResponse: ClaimsResponse)
    }

    interface LoginRequestCallback {
        fun onLoginRequestResponse(loginResponse: LoginResponse)
    }

    interface RegistrationRequestCallback {
        fun onRegistrationRequestResponse(registerResponse: RegisterResponse)
    }

    interface PostClaimRequestCallback {
        fun onPostClaimRequestResponse(postClaimResponse: PostClaimResponse)
    }
}