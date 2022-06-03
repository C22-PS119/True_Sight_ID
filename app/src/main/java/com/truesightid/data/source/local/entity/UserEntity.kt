package com.truesightid.data.source.local.entity


data class UserEntity(
    val id: String,
    val apiKey: String,
    val username: String,
    val email: String,
    val password: String,
    val votes: HashMap<Int, Int>
)