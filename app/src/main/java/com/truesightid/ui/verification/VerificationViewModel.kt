package com.truesightid.ui.verification

import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.*

class VerificationViewModel (private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun sendEmailVerification(request: SendEmailVerificationRequest) =
        mTrueSightRepository.sendEmailVerification(request)
    fun confirmVerificationCode(request: ConfirmEmailVerificationRequest) =
        mTrueSightRepository.confirmEmailVerification(request)
}