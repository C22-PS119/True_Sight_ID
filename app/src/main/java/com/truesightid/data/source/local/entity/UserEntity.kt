package com.truesightid.data.source.local.entity


data class UserEntity(
    val id: Int,
    val apiKey: String,
    val username: String,
    val fullname: String,
    val avatar: String,
    val email: String,
    val password: String,
    val bookmark: ArrayList<Int>,
    val votes: HashMap<Int, Int>
)