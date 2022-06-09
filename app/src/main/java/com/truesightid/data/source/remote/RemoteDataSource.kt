package com.truesightid.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.truesightid.api.ApiHelper
import com.truesightid.data.source.local.entity.ClaimEntity
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
                    resultLogin.value = ApiResponse.error(
                        loginResponse.message ?: "Failed to GET message",
                        LoginResponse()
                    )
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
                    if (registerResponse.status == "success")
                        resultRegister.value = ApiResponse.success(registerResponse)
                    else
                        resultRegister.value = ApiResponse.error(
                            registerResponse.message ?: "Failed to GET message",
                            RegistrationResponse()
                        )
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
                if (postProfileResponse.status == "success")
                    resultPost.value = ApiResponse.success(postProfileResponse)
                else
                    resultPost.value = ApiResponse.error(
                        postProfileResponse.message ?: "Failed to GET message",
                        PostProfileResponse()
                    )
            }

        })
        return resultPost
    }

    fun postProfileRequest(request: EditProfileRequest): LiveData<ApiResponse<PostProfileResponse>> {
        val resultPost = MutableLiveData<ApiResponse<PostProfileResponse>>()
        apiHelper.postProfileResponse(request, object : PostProfileRequestCallback {
            override fun onPostProfileRequestResponse(postProfileResponse: PostProfileResponse) {
                if (postProfileResponse.status == "success")
                    resultPost.value = ApiResponse.success(postProfileResponse)
                else
                    resultPost.value = ApiResponse.error(
                        postProfileResponse.message ?: "Failed to GET message",
                        PostProfileResponse()
                    )
            }

        })
        return resultPost
    }

    fun getUserProfileRequest(request: GetProfileRequest): LiveData<ApiResponse<UserResponse>> {
        val resultPost = MutableLiveData<ApiResponse<UserResponse>>()
        apiHelper.getUserProfileResponse(request, object : GetProfileRequestCallback {
            override fun onGetUserProfileRequestResponse(userProfileResponse: UserResponse) {
                if (userProfileResponse.status == "success")
                    resultPost.value = ApiResponse.success(userProfileResponse)
                else
                    resultPost.value = ApiResponse.error(
                        userProfileResponse.message ?: "Failed to GET message",
                        UserResponse()
                    )
            }

        })
        return resultPost
    }

    fun setPasswordRequest(request: SetPasswordRequest): LiveData<ApiResponse<SetPasswordResponse>> {
        val resultPost = MutableLiveData<ApiResponse<SetPasswordResponse>>()
        apiHelper.getSetPasswordResponse(
            request,
            object : GetSetUserPasswordRequestResponseCallback {
                override fun onGetSetPasswordRequestResponse(setPasswordResponse: SetPasswordResponse) {
                    if (setPasswordResponse.status == "success")
                        resultPost.value = ApiResponse.success(setPasswordResponse)
                    else
                        resultPost.value = ApiResponse.error(
                            setPasswordResponse.message ?: "Failed to GET message",
                            SetPasswordResponse()
                        )
                }

            })
        return resultPost
    }

    fun getMyClaimRequest(request: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>> {
        val resultClaims = MutableLiveData<ApiResponse<List<ClaimEntity>>>()
        apiHelper.getMyClaims(request, object : MyClaimRequestCallback {
            override fun onMyClaimRequestResponse(myClaimResponse: MyClaimResponse) {
                val responseData = myClaimResponse.data
                val claimList = ArrayList<ClaimEntity>()
                if (responseData != null) {
                    for (item in responseData) {
                        val claim = item?.dateCreated?.let {
                            ClaimEntity(
                                item.id as Int,
                                item.title as String,
                                item.authorUsername as String,
                                item.description as String,
                                item.attachment as List<String>,
                                item.fake as Int,
                                item.upvote as Int,
                                item.downvote as Int,
                                it.toFloat()
                            )
                        }
                        claimList.add(claim as ClaimEntity)
                    }
                }
                resultClaims.value = ApiResponse.success(claimList)
            }
        })
        return resultClaims
    }

    fun getMyBookmarkRequest(request: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>> {
        val resultClaims = MutableLiveData<ApiResponse<List<ClaimEntity>>>()
        apiHelper.getMyBookmark(request, object : MyBookmarksCallback {
            override fun onMyBookmarksRequestResponse(myBookmarkResponse: MyBookmarkResponse) {
                val responseData = myBookmarkResponse.data
                val claimList = ArrayList<ClaimEntity>()
                if (responseData != null) {
                    for (item in responseData) {
                        val claim = item?.dateCreated?.let {
                            ClaimEntity(
                                item.id as Int,
                                item.title as String,
                                item.authorUsername as String,
                                item.description as String,
                                item.attachment as List<String>,
                                item.fake as Int,
                                item.upvote as Int,
                                item.downvote as Int,
                                it.toFloat()
                            )
                        }
                        claimList.add(claim as ClaimEntity)
                    }
                }
                resultClaims.value = ApiResponse.success(claimList)
            }
        })
        return resultClaims
    }

    fun upVoteRequestById(api_key: String, id: Int) {
        apiHelper.voteByClaimIdRequest(true, api_key, id)
    }

    fun downVoteRequestById(api_key: String, id: Int) {
        apiHelper.voteByClaimIdRequest(false, api_key, id)
    }

    fun addBookmarkById(request: AddRemoveBookmarkRequest) {
        apiHelper.addRemoveBookmarkByClaimId(true, request)
    }

    fun removeBookmarkById(request: AddRemoveBookmarkRequest) {
        apiHelper.addRemoveBookmarkByClaimId(false, request)
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

    interface GetSetUserPasswordRequestResponseCallback {
        fun onGetSetPasswordRequestResponse(userSetPasswordResponse: SetPasswordResponse)
    }

    interface MyClaimRequestCallback {
        fun onMyClaimRequestResponse(myClaimResponse: MyClaimResponse)
    }

    interface MyBookmarksCallback {
        fun onMyBookmarksRequestResponse(myBookmarkResponse: MyBookmarkResponse)
    }
}