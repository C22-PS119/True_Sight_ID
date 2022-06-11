package com.truesightid.utils

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.utils.extension.toJson
import com.truesightid.utils.extension.toModel


object Prefs : KotprefModel() {

    private const val PREF_NAME = "darkmode.pref"
    private var sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    var isLogin by booleanPref(false)
    var user by stringPref()

    fun setUser(data: UserEntity?) {
        user = data.toJson()
    }

    fun getUser(): UserEntity? {
        if (user.isEmpty()) return null
        return user.toModel(UserEntity::class.java)
    }

    fun isDarkMode(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun setDarkMode(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

}
