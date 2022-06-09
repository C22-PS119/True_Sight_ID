package com.truesightid.data.source.remote.request

data class ConfirmEmailVerificationRequest (
    val user_id: Int,
    val verification_code: String
)