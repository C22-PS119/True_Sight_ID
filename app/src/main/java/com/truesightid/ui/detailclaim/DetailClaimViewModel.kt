package com.truesightid.ui.detailclaim

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest

class DetailClaimViewModel(private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun addBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.addBookmarkById(request)

    fun removeBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.removeBookmarkById(request)
}