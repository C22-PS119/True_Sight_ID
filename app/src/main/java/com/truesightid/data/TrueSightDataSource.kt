package com.truesightid.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.response.LoginResponse
import com.truesightid.utils.Resource

interface TrueSightDataSource {
    fun getAllClaims(): LiveData<Resource<PagedList<ClaimEntity>>>

    fun upVoteClaimById(id: Int)

    fun downVoteClaimById(id: Int)

    fun loginRequest(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>>

//    fun getNewsPrediction(): LiveData<Resource<NewsPredictionEntity>>
}