package com.truesightid.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.inyongtisto.myhelper.extension.logs
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.data.source.local.room.LocalDataSource
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.RemoteDataSource
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.response.ClaimsResponse
import com.truesightid.utils.AppExecutors
import com.truesightid.utils.Prefs
import com.truesightid.utils.Resource
import kotlinx.coroutines.flow.flow

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

    override fun loginRequest(request: LoginRequest) = flow {
        emit(Resource.loading(null))
        try {
            remoteDataSource.loginRequest(request).let {
                if (it?.status == "success") {
                    Prefs.isLogin = true
                    val data = it.data
                    val user = UserEntity(
                        apiKey = data?.apiKey as String,
                        id = data.userId as Int,
                        username = data.username as String,
                        email = data.email as String,
                        password = null
                    )
                    Prefs.setUser(user)
                    emit(Resource.success(user))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
            logs("Error:" + e.message)
        }
    }

    override fun upVoteClaimById(id: Int) {
        remoteDataSource.upVoteRequestById(id)
    }

    override fun downVoteClaimById(id: Int) {
        remoteDataSource.downVoteRequestById(id)
    }

    override fun getAllClaims(): LiveData<Resource<PagedList<ClaimEntity>>> {
        return object : NetworkBoundResource<PagedList<ClaimEntity>, ClaimsResponse>(appExecutor) {
            override fun loadFromDB(): LiveData<PagedList<ClaimEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllClaims(), config).build()
            }


            override fun shouldFetch(data: PagedList<ClaimEntity>?): Boolean =
                data == null || data.isEmpty()


            override fun createCall(): LiveData<ApiResponse<ClaimsResponse>> =
                remoteDataSource.getAllClaims()

            override fun saveCallResult(data: ClaimsResponse) {
                val body = data.data
                val claimList = ArrayList<ClaimEntity>()
                if (body != null) {
                    for (response in body) {
                        val claim = ClaimEntity(
                            response?.id as Int,
                            response.title as String,
                            response.authorId.toString(),
                            response.description as String,
                            image = null,
                            response.fake as Int,
                            response.upvote as Int,
                            response.downvote as Int,
                            response.dateCreated as Int
                        )
                        claimList.add(claim)
                    }
                }
                localDataSource.insertClaims(claimList)
            }
        }.asLiveData()
    }
}