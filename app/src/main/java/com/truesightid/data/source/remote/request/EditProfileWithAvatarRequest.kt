package com.truesightid.data.source.remote.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class EditProfileWithAvatarRequest (
    val apiKey: String,
    val full_name: RequestBody,
    val email: RequestBody,
    val avatar: MultipartBody.Part?
    )