package com.truesightid.ui.myclaim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.request.MyDataRequest

class MyClaimViewModel(private val trueSightRepository: TrueSightRepository) : ViewModel() {
    fun getMyClaims(request: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>> =
        trueSightRepository.getMyClaims(request)

    fun upvoteClaimById(api_key: String, id: Int) = trueSightRepository.upVoteClaimById(api_key, id)
    fun downvoteClaimById(api_key: String, id: Int) =
        trueSightRepository.downVoteClaimById(api_key, id)
    fun deleteClaimById(api_key: String, id: Int, onSuccess: (success:Boolean) -> Unit) =
        trueSightRepository.deleteClaimById(api_key, id, onSuccess)
}