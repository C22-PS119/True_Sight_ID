package com.truesightid.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("dataname")
	val dataname: String? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(
	@field:SerializedName("api_key")
	val apiKey: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("username")
    val username: String? = null,

	@field:SerializedName("email")
    val email: String? = null
)
