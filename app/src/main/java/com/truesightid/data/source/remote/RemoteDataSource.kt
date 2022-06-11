package com.truesightid.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.truesightid.api.ApiHelper
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.entity.CommentEntity
import com.truesightid.data.source.remote.request.*
import com.truesightid.data.source.remote.response.*
import com.truesightid.utils.StringSeparatorUtils

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

    fun getAllClaims(request: ClaimRequest): LiveData<ApiResponse<GetClaimsResponse>> {
        val resultClaims = MutableLiveData<ApiResponse<GetClaimsResponse>>()
        apiHelper.getClaimsRequest(request, object : ClaimsRequestCallback {
            override fun onClaimsRequestResponse(getClaimsResponse: GetClaimsResponse) {
                resultClaims.value = ApiResponse.success(getClaimsResponse)
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
                override fun onGetSetPasswordRequestResponse(userSetPasswordResponse: SetPasswordResponse) {
                    if (userSetPasswordResponse.status == "success")
                        resultPost.value = ApiResponse.success(userSetPasswordResponse)
                    else
                        resultPost.value = ApiResponse.error(
                            userSetPasswordResponse.message ?: "Failed to GET message",
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
                                it.toFloat(),
                                StringSeparatorUtils.separateUrlResponse(item.url)
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
                                it.toFloat(),
                                StringSeparatorUtils.separateUrlResponse(item.url)
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

    fun sendEmailVerification(request: SendEmailVerificationRequest): LiveData<ApiResponse<EmailVerificationRespond>> {
        val resultPost = MutableLiveData<ApiResponse<EmailVerificationRespond>>()
        apiHelper.sendEmailVerification(request, object : EmailVerificationRequestResponseCallback {
            override fun onEmailVerificationRequestResponse(emailVerificationRespond: EmailVerificationRespond) {
                if (emailVerificationRespond.status == "success")
                    resultPost.value = ApiResponse.success(emailVerificationRespond)
                else
                    resultPost.value = ApiResponse.error(
                        emailVerificationRespond.message ?: "Failed to GET message",
                        EmailVerificationRespond()
                    )
            }

        })
        return resultPost
    }

    fun confirmEmailVerification(request: ConfirmEmailVerificationRequest): LiveData<ApiResponse<ConfirmVerificationRespond>> {
        val resultPost = MutableLiveData<ApiResponse<ConfirmVerificationRespond>>()
        apiHelper.confirmEmailVerification(
            request,
            object : ConfirmVerificationRequestResponseCallback {
                override fun onConfirmVerificationRequestResponse(confirmEmailVerificationResponse: ConfirmVerificationRespond) {
                    if (confirmEmailVerificationResponse.status == "success")
                        resultPost.value = ApiResponse.success(confirmEmailVerificationResponse)
                    else
                        resultPost.value = ApiResponse.error(
                            confirmEmailVerificationResponse.message ?: "Failed to GET message",
                            ConfirmVerificationRespond()
                        )
                }

            })
        return resultPost
    }

    fun resetPasswordRequest(request: ResetPasswordRequest): LiveData<ApiResponse<SetPasswordResponse>> {
        val resultPost = MutableLiveData<ApiResponse<SetPasswordResponse>>()
        apiHelper.resetPassword(request, object : GetSetUserPasswordRequestResponseCallback {
            override fun onGetSetPasswordRequestResponse(userSetPasswordResponse: SetPasswordResponse) {
                if (userSetPasswordResponse.status == "success")
                    resultPost.value = ApiResponse.success(userSetPasswordResponse)
                else
                    resultPost.value = ApiResponse.error(
                        userSetPasswordResponse.message ?: "Failed to GET message",
                        SetPasswordResponse()
                    )
            }

        })
        return resultPost
    }

    fun getCommentsRequest(request: GetCommentsRequest): LiveData<ApiResponse<List<CommentEntity>>> {
        val resultComments = MutableLiveData<ApiResponse<List<CommentEntity>>>()
        apiHelper.getComments(request, object : GetCommentsCallback {
            override fun onGetCommentsRequestResponse(getCommentsResponse: GetCommentsResponse) {
                val commentList = ArrayList<CommentEntity>()
                val data = getCommentsResponse.data
                if (data != null) {
                    for (response in data) {
                        commentList.add(
                            CommentEntity(
                                response?.id as Int,
                                response.authorId as Int,
                                response.username as String,
                                response.commentText as String,
                                response.dateCreated as Double,
                                response.claimId as Int,
                                response.profileAvatar.toString()
                            )
                        )
                    }
                }
                resultComments.value = ApiResponse.success(commentList)
            }
        })
        return resultComments
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

    fun addCommentById(request: AddCommentRequest) {
        apiHelper.addComment(request)
    }


    interface ClaimsRequestCallback {
        fun onClaimsRequestResponse(getClaimsResponse: GetClaimsResponse)
    }

    interface EmailVerificationRequestResponseCallback {
        fun onEmailVerificationRequestResponse(emailVerificationRespond: EmailVerificationRespond)
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

    interface ConfirmVerificationRequestResponseCallback {
        fun onConfirmVerificationRequestResponse(confirmEmailVerificationResponse: ConfirmVerificationRespond)
    }

    interface MyClaimRequestCallback {
        fun onMyClaimRequestResponse(myClaimResponse: MyClaimResponse)
    }

    interface MyBookmarksCallback {
        fun onMyBookmarksRequestResponse(myBookmarkResponse: MyBookmarkResponse)
    }

    interface GetCommentsCallback {
        fun onGetCommentsRequestResponse(getCommentsResponse: GetCommentsResponse)
    }
}