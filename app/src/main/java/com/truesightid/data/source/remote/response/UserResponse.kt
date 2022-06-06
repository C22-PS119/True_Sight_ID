package com.truesightid.data.source.remote.response

import com.google.gson.annotations.SerializedName


data class UserResponse(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: User? = null,

    @field:SerializedName("dataname")
    val dataname: String? = null,

    @field:SerializedName("source")
    val source: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)