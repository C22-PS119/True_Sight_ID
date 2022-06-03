package com.truesightid.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.truesightid.api.ApiHelper
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.response.ClaimsResponse
import com.truesightid.data.source.remote.response.LoginResponse
import com.truesightid.data.source.remote.response.RegisterResponse

class RemoteDataSource private constructor(private val apiHelper: ApiHelper) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: ApiHelper): RemoteDataSource = instance ?: synchronized(this) {
            RemoteDataSource(helper).apply { instance = this }
        }
    }

    fun loginRequest(request: LoginRequest): LoginResponse? {
        var resultLogin: LoginResponse? = null
        apiHelper.loginRequest(request, object : LoginRequestCallback {
            override fun onLoginRequestResponse(loginResponse: LoginResponse) {
                resultLogin = loginResponse
            }
        })
        return resultLogin
    }

    fun registrationRequest(
        username: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<RegisterResponse>> {
        val resultRegister = MutableLiveData<ApiResponse<RegisterResponse>>()
        apiHelper.registrationRequest(username, email, password,
            object : RegistrationRequestCallback {
                override fun onRegistrationRequestResponse(registerResponse: RegisterResponse) {
                    resultRegister.value = ApiResponse.success(registerResponse)
                }
            })
        return resultRegister
    }

    fun getAllClaims(): LiveData<ApiResponse<ClaimsResponse>> {
        val resultClaims = MutableLiveData<ApiResponse<ClaimsResponse>>()
        apiHelper.getClaimsRequest(object : ClaimsRequestCallback {
            override fun onClaimsRequestResponse(claimsResponse: ClaimsResponse) {
                resultClaims.value = ApiResponse.success(claimsResponse)
            }
        })
        return resultClaims
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
}