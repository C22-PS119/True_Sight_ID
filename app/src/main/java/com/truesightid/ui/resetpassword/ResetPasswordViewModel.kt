package com.truesightid.ui.resetpassword

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.ResetPasswordRequest

class ResetPasswordViewModel (private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun resetPassword(request: ResetPasswordRequest) =
        mTrueSightRepository.resetPassword(request)
}