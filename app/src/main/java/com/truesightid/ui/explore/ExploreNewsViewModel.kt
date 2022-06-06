package com.truesightid.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.request.ClaimRequest
import com.truesightid.utils.Resource
import kotlinx.coroutines.launch

class ExploreNewsViewModel(private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun getClaims(request: ClaimRequest): LiveData<Resource<PagedList<ClaimEntity>>> =
        mTrueSightRepository.getAllClaims(request)

    fun upvoteClaimById(id: Int) = mTrueSightRepository.upVoteClaimById(id)
    fun downvoteClaimById(id: Int) = mTrueSightRepository.downVoteClaimById(id)
    fun deleteLocalClaims() {
        viewModelScope.launch {
            mTrueSightRepository.deleteLocalClaims()
        }
    }
}