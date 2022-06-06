package com.truesightid.utils

import androidx.lifecycle.ViewModelProvider
import com.chibatching.kotpref.KotprefModel
import com.inyongtisto.myhelper.extension.toJson
import com.inyongtisto.myhelper.extension.toModel
import com.truesightid.api.ApiHelper
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.data.source.remote.RemoteDataSource
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.editprofile.SetProfileViewModel

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
