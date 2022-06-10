package com.truesightid.data.source.remote.request

data class AddCommentRequest(
    val apiKey: String,
    val id: Int,
    val comment: String
)
