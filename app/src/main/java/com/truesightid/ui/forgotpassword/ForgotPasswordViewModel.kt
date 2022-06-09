package com.truesightid.ui.forgotpassword
import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.remote.request.ConfirmEmailVerificationRequest
import com.truesightid.data.source.remote.request.SendEmailVerificationRequest

class ForgotPasswordViewModel(private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun sendEmailVerification(request: SendEmailVerificationRequest) =
        mTrueSightRepository.sendEmailVerification(request)
}