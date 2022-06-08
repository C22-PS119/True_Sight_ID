package com.truesightid.data.source.remote.request

data class SetPasswordRequest (
    val apiKey: String,
    val current_password: String,
    val new_password: String
)