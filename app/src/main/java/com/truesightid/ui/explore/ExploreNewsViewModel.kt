package com.truesightid.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.GetClaimsRequest
import com.truesightid.utils.FilterSearch
import com.truesightid.utils.Resource

class ExploreNewsViewModel(private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun getClaims(request: GetClaimsRequest): LiveData<Resource<PagedList<ClaimEntity>>> =
        mTrueSightRepository.getAllClaims(request, null)

    fun getClaimsWithFilter(request: GetClaimsRequest, filter: FilterSearch): LiveData<Resource<PagedList<ClaimEntity>>> =
        mTrueSightRepository.getAllClaims(request, filter)

    fun upvoteClaimById(api_key: String, id: Int) =
        mTrueSightRepository.upVoteClaimById(api_key, id)

    fun downvoteClaimById(api_key: String, id: Int) =
        mTrueSightRepository.downVoteClaimById(api_key, id)

    fun addBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.addBookmarkById(request)

    fun removeBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.removeBookmarkById(request)
}