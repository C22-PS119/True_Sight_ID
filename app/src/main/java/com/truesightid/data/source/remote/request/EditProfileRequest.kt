package com.truesightid.data.source.remote.request

import okhttp3.RequestBody

data class EditProfileRequest(
    val apiKey: String,
    val full_name: RequestBody,
    val email: RequestBody
)