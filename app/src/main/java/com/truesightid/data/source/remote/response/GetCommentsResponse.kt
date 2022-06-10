package com.truesightid.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GetCommentsResponse(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: List<CommentItem?>? = null,

    @field:SerializedName("dataname")
    val dataname: String? = null,

    @field:SerializedName("source")
    val source: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class CommentItem(

    @field:SerializedName("comment_text")
    val commentText: String? = null,

    @field:SerializedName("profile_avatar")
    val profileAvatar: String? = null,

    @field:SerializedName("post_id")
    val claimId: Int? = null,

    @field:SerializedName("date_created")
    val dateCreated: Double? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("author_id")
    val authorId: Int? = null,

    @field:SerializedName("username")
    val username: String? = null
)
