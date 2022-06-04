package com.truesightid.ui.add_claim

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.PostClaimRequest

class AddClaimViewModel(private val repository: TrueSightRepository) : ViewModel() {
    fun addClaim(request: PostClaimRequest) = repository.postClaim(request)
}