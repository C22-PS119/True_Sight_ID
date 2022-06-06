package com.truesightid.utils

import com.chibatching.kotpref.KotprefModel
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.utils.extension.toJson
import com.truesightid.utils.extension.toModel

object Prefs : KotprefModel() {

    var isLogin by booleanPref(false)
    var user by stringPref()

    fun setUser(data: UserEntity?) {
        user = data.toJson()
    }

    fun getUser(): UserEntity? {
        if (user.isEmpty()) return null
        return user.toModel(UserEntity::class.java)
    }
}
