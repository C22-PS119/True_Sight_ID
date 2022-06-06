package com.truesightid.data.source.remote.request

data class RegistrationRequest(
    val apiKey: String,
    val username: String,
    val fullname: String,
    val email: String,
    val password: String
)