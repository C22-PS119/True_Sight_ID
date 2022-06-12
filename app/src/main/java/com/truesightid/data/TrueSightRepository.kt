package com.truesightid.data


import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.entity.CommentEntity
import com.truesightid.data.source.local.room.LocalDataSource
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.RemoteDataSource
import com.truesightid.data.source.remote.request.*
import com.truesightid.data.source.remote.response.*
import com.truesightid.utils.AppExecutors
import com.truesightid.utils.FilterSearch
import com.truesightid.utils.Resource
import com.truesightid.utils.StringSeparatorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TrueSightRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutor: AppExecutors
) : TrueSightDataSource {
    companion object {
        @Volatile
        private var instance: TrueSightRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): TrueSightRepository =
            instance ?: synchronized(this) {
                TrueSightRepository(remoteData, localData, appExecutors).apply {
                    instance = this
                }
            }
    }

    override fun upVoteClaimById(api_key: String, id: Int) =
        remoteDataSource.upVoteRequestById(api_key, id)


    override fun downVoteClaimById(api_key: String, id: Int) =
        remoteDataSource.downVoteRequestById(api_key, id)

    override fun deleteClaimById(api_key: String, id: Int, onSuccess: (success:Boolean) -> Unit) =
        remoteDataSource.deleteRequestById(api_key, id, onSuccess)

    override fun addBookmarkById(addRemoveBookmarkRequest: AddRemoveBookmarkRequest) =
        remoteDataSource.addBookmarkById(addRemoveBookmarkRequest)

    override fun removeBookmarkById(addRemoveBookmarkRequest: AddRemoveBookmarkRequest) =
        remoteDataSource.removeBookmarkById(addRemoveBookmarkRequest)

    override fun addCommentById(addCommentRequest: AddCommentRequest) =
        remoteDataSource.addCommentById(addCommentRequest)

    override fun getClaimsBySearch(claimsRequest: GetClaimsRequest): LiveData<ApiResponse<List<ClaimEntity>>> =
        remoteDataSource.getClaimsBySearch(claimsRequest)

    override fun sendEmailVerification(sendEmailVerificationRequest: SendEmailVerificationRequest): LiveData<ApiResponse<EmailVerificationRespond>> =
        remoteDataSource.sendEmailVerification(sendEmailVerificationRequest)

    override fun confirmEmailVerification(confirmEmailVerificationRequest: ConfirmEmailVerificationRequest): LiveData<ApiResponse<ConfirmVerificationRespond>> =
        remoteDataSource.confirmEmailVerification(confirmEmailVerificationRequest)

    override fun resetPassword(resetPasswordRequest: ResetPasswordRequest): LiveData<ApiResponse<SetPasswordResponse>> =
        remoteDataSource.resetPasswordRequest(resetPasswordRequest)

    override fun getCommentsByClaimId(getCommentsRequest: GetCommentsRequest): LiveData<ApiResponse<List<CommentEntity>>> =
        remoteDataSource.getCommentsRequest(getCommentsRequest)

    override fun loginRequest(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>> =
        remoteDataSource.loginRequest(loginRequest)

    override fun postClaim(postClaimRequest: PostClaimRequest): LiveData<ApiResponse<PostClaimResponse>> =
        remoteDataSource.postClaimRequest(postClaimRequest)

    override fun postProfileWithAvatar(editProfileWithAvatarRequest: EditProfileWithAvatarRequest): LiveData<ApiResponse<PostProfileResponse>> =
        remoteDataSource.postProfileWithAvatarRequest(editProfileWithAvatarRequest)

    override fun postProfile(editProfileRequest: EditProfileRequest): LiveData<ApiResponse<PostProfileResponse>> =
        remoteDataSource.postProfileRequest(editProfileRequest)

    override fun getUserProfile(getProfileRequest: GetProfileRequest): LiveData<ApiResponse<UserResponse>> =
        remoteDataSource.getUserProfileRequest(getProfileRequest)

    override fun setPassword(setPasswordRequest: SetPasswordRequest): LiveData<ApiResponse<SetPasswordResponse>> =
        remoteDataSource.setPasswordRequest(setPasswordRequest)

    override fun registrationRequest(registrationRequest: RegistrationRequest): LiveData<ApiResponse<RegistrationResponse>> =
        remoteDataSource.registrationRequest(registrationRequest)

    override fun getMyClaims(myDataRequest: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>> =
        remoteDataSource.getMyClaimRequest(myDataRequest)

    override fun getMyBookmarks(myDataRequest: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>> =
        remoteDataSource.getMyBookmarkRequest(myDataRequest)

    override fun deleteLocalClaimByID(id: Int) {
        localDataSource.deleteClaimByID(id)
    }

    override fun getDeletedClaims(apiKey:String, onFinished: (success: Boolean, deletedClaims: ArrayList<Int>) -> Unit) {
       remoteDataSource.getAvailableClaimsID(apiKey){ success, response ->
           val DeletedClaims = ArrayList<Int>()
           if (success){
               GlobalScope.launch(Dispatchers.IO){
                   val LocalAvailableClaims = localDataSource.queryAllClaimsId()
                   val OnlineAvailableClaims = response?.data
                   if (OnlineAvailableClaims != null){

                       for (entity in LocalAvailableClaims){
                           if (entity.id !in OnlineAvailableClaims){
                               DeletedClaims.add(entity.id)
                               deleteLocalClaimByID(entity.id)
                           }
                       }
                       onFinished(true, DeletedClaims)
                   }else{
                       onFinished(false, DeletedClaims)
                   }
               }
           }else{
               onFinished(false, DeletedClaims)
           }
       }
    }

    override fun getAllClaims(
        request: GetClaimsRequest,
        filter: FilterSearch?
    ): LiveData<Resource<PagedList<ClaimEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ClaimEntity>, GetClaimsResponse>(appExecutor) {
            override fun loadFromDB(): LiveData<PagedList<ClaimEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(1)
                    .setPageSize(1)
                    .build()
                if (filter == null) {
                    return LivePagedListBuilder(
                        localDataSource.getClaims(request.keyword),
                        config
                    ).build()
                } else {
                    return LivePagedListBuilder(
                        localDataSource.getClaimsWithFilter(
                            request.keyword,
                            filter.sortBy,
                            filter.type,
                            filter.optDate,
                            filter.dateFrom,
                            filter.dateTo
                        ),
                        config
                    ).build()
                }
            }


            override fun shouldFetch(data: PagedList<ClaimEntity>?): Boolean =
                data == null || data.isEmpty()


            override fun createCall(): LiveData<ApiResponse<GetClaimsResponse>> =
                remoteDataSource.getAllClaims(request)

            override fun saveCallResult(data: GetClaimsResponse) {
                val body = data.data
                val claimList = ArrayList<ClaimEntity>()
                if (body != null) {
                    for (response in body) {
                        val claim = response.dateCreated?.let {
                            ClaimEntity(
                                response.id as Int,
                                response.title as String,
                                response.authorUsername as String,
                                response.description as String,
                                response.attachment as List<String>,
                                response.fake as Int,
                                response.upvote as Int,
                                response.downvote as Int,
                                it.toFloat(),
                                StringSeparatorUtils.separateUrlResponse(response.url)
                            )
                        }
                        if (claim != null) {
                            claimList.add(claim)
                        }
                    }
                }
                localDataSource.insertClaims(claimList)
            }
        }.asLiveData()
    }
}