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
            simpleQuery.append(" WHERE claimer LIKE '%$keywordSearch%' OR title LIKE '%$keywordSearch%' OR description LIKE '%$keywordSearch%' ORDER BY CAST(date/259200 as INT) DESC, (upvote-downvote) DESC")
        }else{
            simpleQuery.append(" ORDER BY CAST(date/259200 as INT) DESC, (upvote-downvote) DESC")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}