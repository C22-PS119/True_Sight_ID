package com.truesightid.ui.login

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.LoginRequest

class LoginViewModel(
    private val repository: TrueSightRepository
) : ViewModel() {

//    private val _loginData = MutableLiveData<ApiResponse<LoginResponse>>()
//    val loginData: LiveData<ApiResponse<LoginResponse>> = _loginData

    fun login(data: LoginRequest) = repository.loginRequest(data)

//    fun login(data: LoginRequest) {
//        _loginData = repository.loginRequest(data)
//    }
}