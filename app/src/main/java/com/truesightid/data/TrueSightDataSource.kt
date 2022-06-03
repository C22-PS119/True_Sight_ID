package com.truesightid.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrueSightDataSource {
    fun getAllClaims(): LiveData<Resource<PagedList<ClaimEntity>>>

    fun loginRequest(request: LoginRequest): Flow<Resource<out UserEntity>>

    fun upVoteClaimById(id: Int)

    fun downVoteClaimById(id: Int)

//    fun getNewsPrediction(): LiveData<Resource<NewsPredictionEntity>>
}