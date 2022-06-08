package com.truesightid.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MyClaimResponse(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: List<MyClaim?>? = null,

    @field:SerializedName("dataname")
    val dataname: String? = null,

    @field:SerializedName("source")
    val source: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class MyClaim(

    @field:SerializedName("date_created")
    val dateCreated: Double? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("comment_id")
    val commentId: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("upvote")
    val upvote: Int? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("verified_by")
    val verifiedBy: Any? = null,

    @field:SerializedName("downvote")
    val downvote: Int? = null,

    @field:SerializedName("attachment")
    val attachment: List<String?>? = null,

    @field:SerializedName("num_click")
    val numClick: Int? = null,

    @field:SerializedName("fake")
    val fake: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("author_id")
    val authorId: Int? = null,

    @field:SerializedName("author_username")
    val authorUsername: String? = null
)
