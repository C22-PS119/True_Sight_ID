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
import com.truesightid.data.source.remote.response.RegistrationResponse

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
}