package com.truesightid.utils

import android.util.Log
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

    fun getKeywordWithFilterQuery(
        keywordSearch: String,
        sortBy: Int,
        type: Int,
        optDate: Int,
        dateFrom: Long?,
        dateTo: Long?,
        table_name: String
    ): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM $table_name ")
        val conditions = ArrayList<String>()
        when (type){
            1 -> conditions.add("fake=1")
            2 -> conditions.add("fake=0")
        }
        if (optDate == 1){
            if (dateFrom != null)
                conditions.add("date > ${(dateFrom/1000).toInt()}")
            if (dateTo != null)
                conditions.add("date <= ${(dateTo/1000).toInt()}")
        }

        if (!keywordSearch.isNullOrBlank())
            conditions.add("(claimer LIKE '%$keywordSearch%' OR title LIKE '%$keywordSearch%' OR description LIKE '%$keywordSearch%')")

        if (conditions.count() > 0)
            simpleQuery.append("WHERE " + conditions.joinToString(separator = " AND "))

        when (sortBy){
            1 -> simpleQuery.append(" ORDER BY (upvote-downvote) DESC")
            2 -> simpleQuery.append(" ORDER BY (upvote-downvote) ASC")
            3 -> simpleQuery.append(" ORDER BY date DESC")
            4 -> simpleQuery.append(" ORDER BY date ASC")
        }
        Log.d("LOG","QUERY: " + simpleQuery.toString())
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}