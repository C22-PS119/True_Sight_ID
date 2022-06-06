package com.truesightid.api

import android.content.Context
import android.widget.Toast
import com.truesightid.data.source.remote.RemoteDataSource
import com.truesightid.data.source.remote.request.ClaimRequest
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.request.PostClaimRequest
import com.truesightid.data.source.remote.request.RegistrationRequest
import com.truesightid.data.source.remote.response.*
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
        request: RegistrationRequest,
        callback: RemoteDataSource.RegistrationRequestCallback
    ) {
        val client = ApiConfig.getApiService()
            .postRegistrationForm(
                request.apiKey,
                request.username,
                fullname = request.username,
                request.email,
                request.password
            )
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
                    "onRegistrationRequestFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun getClaimsRequest(request: ClaimRequest, callback: RemoteDataSource.ClaimsRequestCallback) {
        val client = ApiConfig.getApiService().getAllClaims(request.apiKey, request.keyword)
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
        val client: Call<VoteResponse> = if (isUpVote) {
            ApiConfig.getApiService().upvoteByClaimID(id)
        } else {
            ApiConfig.getApiService().downVoteByClaimID(id)
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

    fun postResponse(
        request: PostClaimRequest,
        callback: RemoteDataSource.PostClaimRequestCallback
    ) {
        val client = ApiConfig.getApiService().postClaimMultiPart(
            request.apiKey,
            request.title,
            request.description,
            request.fake,
            request.url,
            request.attachment[0]
        )
        client.enqueue(object : Callback<PostClaimResponse> {
            override fun onResponse(
                call: Call<PostClaimResponse>,
                response: Response<PostClaimResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onPostClaimRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<PostClaimResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "onPostClaimFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}