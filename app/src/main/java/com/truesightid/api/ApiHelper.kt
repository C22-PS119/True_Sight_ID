package com.truesightid.api

import android.content.Context
import android.widget.Toast
import com.inyongtisto.myhelper.extension.toRequestBody
import com.truesightid.data.source.remote.RemoteDataSource
import com.truesightid.data.source.remote.request.*
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
                request.username,
                fullname = request.username,
                request.email,
                request.password
            )
        client.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onRegistrationRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
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

    fun voteByClaimIdRequest(isUpVote: Boolean, api_key: String, id: Int) {
        val client: Call<VoteResponse> = if (isUpVote) {
            ApiConfig.getApiService().upvoteByClaimID(
                api_key,
                id
            )
        } else {
            ApiConfig.getApiService().downVoteByClaimID(api_key, id)
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
        val client = if (request.attachment.isNotEmpty()) {
            ApiConfig.getApiService().postClaimMultiPart(
                request.apiKey,
                request.title,
                request.description,
                request.fake,
                request.url,
                request.attachment[0]
            )
        } else {
            ApiConfig.getApiService().postClaimMultiPart(
                request.apiKey,
                request.title,
                request.description,
                request.fake,
                request.url,
                null
            )
        }

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

    fun postProfileWithAvatarResponse(
        request: EditProfileWithAvatarRequest,
        callback: RemoteDataSource.PostProfileRequestCallback
    ) {
        val client = ApiConfig.getApiService().setProfileWithAvatar(
            request.apiKey,
            request.full_name,
            request.email,
            request.avatar
        )
        client.enqueue(object : Callback<PostProfileResponse> {
            override fun onResponse(
                call: Call<PostProfileResponse>,
                response: Response<PostProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onPostProfileRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<PostProfileResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "onSetUserFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun postProfileResponse(
        request: EditProfileRequest,
        callback: RemoteDataSource.PostProfileRequestCallback
    ) {
        val client = ApiConfig.getApiService().setProfile(
            request.apiKey,
            request.full_name,
            request.email
        )
        client.enqueue(object : Callback<PostProfileResponse> {
            override fun onResponse(
                call: Call<PostProfileResponse>,
                response: Response<PostProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onPostProfileRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<PostProfileResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "onSetUserFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun getUserProfileResponse(
        request: GetProfileRequest,
        callback: RemoteDataSource.GetProfileRequestCallback
    ) {
        val client = ApiConfig.getApiService().getProfileByID(
            request.apiKey,
            request.id,
        )
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onGetUserProfileRequestResponse(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "onGetUserFailed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}