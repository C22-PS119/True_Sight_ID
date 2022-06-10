package com.truesightid.ui.detailclaim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.CommentEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.request.AddCommentRequest
import com.truesightid.data.source.remote.request.GetCommentsRequest

class DetailClaimViewModel(private val trueSightRepository: TrueSightRepository) : ViewModel() {
    fun getCommentsByClaimId(getCommentsRequest: GetCommentsRequest): LiveData<ApiResponse<List<CommentEntity>>> =
        trueSightRepository.getCommentsByClaimId(getCommentsRequest)

    fun addCommentByClaimId(addCommentRequest: AddCommentRequest) =
        trueSightRepository.addCommentById(addCommentRequest)
}