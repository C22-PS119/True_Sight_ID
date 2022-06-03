package com.truesightid.ui.main

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.LoginRequest

class MainViewModel(
    private val repository: TrueSightRepository
) : ViewModel() {
    fun getUser(request: LoginRequest) = repository.loginRequest(request)
}