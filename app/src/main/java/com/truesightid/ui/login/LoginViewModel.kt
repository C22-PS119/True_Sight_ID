package com.truesightid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.LoginRequest

class LoginViewModel(private val repository: TrueSightRepository) : ViewModel() {
    fun login(data: LoginRequest) = repository.loginRequest(data).asLiveData()
}