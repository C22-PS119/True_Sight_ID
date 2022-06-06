package com.truesightid.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.request.ClaimRequest
import com.truesightid.utils.Prefs
import com.truesightid.utils.Resource
import kotlinx.coroutines.launch

class ExploreNewsViewModel(private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun getClaims(request: ClaimRequest): LiveData<Resource<PagedList<ClaimEntity>>> =
        mTrueSightRepository.getAllClaims(request)

    fun upvoteClaimById(api_key: String,id: Int) = mTrueSightRepository.upVoteClaimById(api_key,id)
    fun downvoteClaimById(api_key: String,id: Int) = mTrueSightRepository.downVoteClaimById(api_key,id)
    fun deleteLocalClaims() {
        viewModelScope.launch {
            mTrueSightRepository.deleteLocalClaims()
        }
    }
}