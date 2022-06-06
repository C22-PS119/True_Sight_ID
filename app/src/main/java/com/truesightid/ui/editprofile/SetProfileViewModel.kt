package com.truesightid.ui.editprofile

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.EditProfileRequest
import com.truesightid.data.source.remote.request.EditProfileWithAvatarRequest
import com.truesightid.data.source.remote.request.GetProfileRequest

class SetProfileViewModel(private val repository: TrueSightRepository) : ViewModel() {
    fun setProfile(request: EditProfileRequest) = repository.postProfile(request)
    fun setProfileWithAvatar(request: EditProfileWithAvatarRequest) = repository.postProfileWithAvatar(request)
    fun getUserProfile(request: GetProfileRequest) = repository.getUserProfile(request)
}