package com.truesightid.ui.mybookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.MyDataRequest

class MyBookmarkViewModel(private val trueSightRepository: TrueSightRepository) : ViewModel() {
    fun getMyBookmarks(request: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>> =
        trueSightRepository.getMyBookmarks(request)

    fun removeBookmarkById(request: AddRemoveBookmarkRequest) =
        trueSightRepository.removeBookmarkById(request)

    fun upvoteClaimById(api_key: String, id: Int) = trueSightRepository.upVoteClaimById(api_key, id)
    fun downvoteClaimById(api_key: String, id: Int) =
        trueSightRepository.downVoteClaimById(api_key, id)
}