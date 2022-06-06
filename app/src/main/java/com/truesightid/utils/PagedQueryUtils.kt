package com.truesightid.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object PagedQueryUtils {
    const val CLAIMS_ENTITIES = "claim"
    fun getKeywordQuery(
        keywordSearch: String,
        table_name: String
    ): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM $table_name")
        if (keywordSearch != "") {
            simpleQuery.append(" WHERE claimer LIKE '%$keywordSearch%' OR title LIKE '%$keywordSearch%' OR description LIKE '%$keywordSearch%'")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}