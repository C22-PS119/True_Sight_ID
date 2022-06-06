package com.truesightid.data.source.remote.request

import okhttp3.MultipartBody

data class PostClaimRequest(
    val apiKey: String,
    val title: String,
    val description: String,
    val fake: Int,
    val url: String,
    val attachment: List<MultipartBody.Part>
)