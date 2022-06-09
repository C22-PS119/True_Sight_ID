package com.truesightid.data.source.remote.request

data class ResetPasswordRequest (
    val reset_key: String,
    val new_password: String
)