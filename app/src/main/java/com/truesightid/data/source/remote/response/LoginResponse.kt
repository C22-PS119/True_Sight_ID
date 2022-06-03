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

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("bookmarks")
	val bookmarks: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: Int? = null,

	@field:SerializedName("verified")
	val verified: Int? = null,

	@field:SerializedName("votes")
	val votes: String? = null,

	@field:SerializedName("avatar")
	val avatar: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("apioauth")
	val apioauth: Any? = null,

	@field:SerializedName("username")
	val username: String? = null
)
