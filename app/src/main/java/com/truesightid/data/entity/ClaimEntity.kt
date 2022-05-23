package com.truesightid.data.entity

data class ClaimEntity(
    var id: Int,
    var title: String,
    var claimer: String,
    var description: String,
    var image: String,
    var fake: Boolean,
    var upvote: Int,
    var downvote: Int,
    var date: String,
    var voteCount: Int,
    var bookmark: Boolean
)

