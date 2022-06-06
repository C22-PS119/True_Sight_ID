package com.truesightid.data.source.local.room

import androidx.paging.DataSource
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.utils.PagedQueryUtils
import com.truesightid.utils.PagedQueryUtils.CLAIMS_ENTITIES

class LocalDataSource(
    private val trueSightDao: TrueSightDao
) {
    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(trueSightDao: TrueSightDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(trueSightDao)
    }

    fun getClaims(keyword: String): DataSource.Factory<Int, ClaimEntity> = trueSightDao.getClaims(PagedQueryUtils.getKeywordQuery(keyword, CLAIMS_ENTITIES))

    fun insertClaims(claims: List<ClaimEntity>) = trueSightDao.insertAllClaims(claims)
    fun deleteLocalClaims() = trueSightDao.deleteAllLocalClaims()
}