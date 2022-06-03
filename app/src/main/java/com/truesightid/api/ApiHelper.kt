package com.truesightid.api

import android.content.Context
import android.widget.Toast
import com.truesightid.data.source.remote.RemoteDataSource
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.response.ClaimsResponse
import com.truesightid.data.source.remote.response.LoginResponse
import com.truesightid.data.source.remote.response.RegisterResponse
import com.truesightid.data.source.remote.response.VoteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiHelper(val context: Context) {
    fun loginRequest(request: LoginRequest, callback: RemoteDataSource.LoginRequestCallback) {
        val client = ApiConfig.getApiService()
            .postLoginForm(request.email, request.password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onLoginRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "onLoginRequestFailed: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun registrationRequest(
        username: String,
        email: String,
        password: String,
        callback: RemoteDataSource.RegistrationRequestCallback
    ) {
        val client = ApiConfig.getApiService()
            .postRegistrationForm(username, fullname = username, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onRegistrationRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "onRegisterationRequestFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun getClaimsRequest(callback: RemoteDataSource.ClaimsRequestCallback) {
        val client = ApiConfig.getApiService().getAllClaims("")
        client.enqueue(object : Callback<ClaimsResponse> {
            override fun onResponse(
                call: Call<ClaimsResponse>,
                response: Response<ClaimsResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onClaimsRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<ClaimsResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "onClaimRequestFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun voteByClaimIdRequest(isUpVote: Boolean, id: Int) {
        lateinit var client: Call<*>
        if (isUpVote) {
            client = ApiConfig.getApiService().upvoteByClaimID(id)
        } else {
            client = ApiConfig.getApiService().downVoteByClaimID(id)
        }
        client.enqueue(object : Callback<VoteResponse> {
            override fun onResponse(
                call: Call<VoteResponse>,
                response: Response<VoteResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Vote Successful ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<VoteResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "onVoteRequestFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        })
    }
}