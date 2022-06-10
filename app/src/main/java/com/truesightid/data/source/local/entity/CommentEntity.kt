package com.truesightid.data.source.local.entity

data class CommentEntity(
    val comment_id: Int,
    val author_id: Int,
    val username: String,
    val comment: String,
    val dateCreated: Double,
    val claim_id: Int,
    val avatar: String
)


