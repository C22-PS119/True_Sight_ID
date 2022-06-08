package com.truesightid.ui.profile

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.GetProfileRequest

class ProfileViewModel(private val repository: TrueSightRepository) : ViewModel() {
    fun getUserProfile(request: GetProfileRequest) = repository.getUserProfile(request)
}