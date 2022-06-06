package com.truesightid.ui.signup

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.RegistrationRequest

class SignupViewModel(private val repository: TrueSightRepository) : ViewModel() {
    fun signUpRequest(request: RegistrationRequest) = repository.registrationRequest(request)
}