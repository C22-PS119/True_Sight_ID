package com.truesightid.data.source.local.room

import androidx.paging.DataSource
import com.truesightid.data.source.local.entity.ClaimEntity

class LocalDataSource(
    private val trueSightDao: TrueSightDao
) {
    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(trueSightDao: TrueSightDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(trueSightDao)
    }

    fun getAllClaims(): DataSource.Factory<Int, ClaimEntity> = trueSightDao.getAllClaims()

    fun insertClaims(claims: List<ClaimEntity>) = trueSightDao.insertAllClaims(claims)
}