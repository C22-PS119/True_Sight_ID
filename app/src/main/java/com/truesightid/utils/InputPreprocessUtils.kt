package com.truesightid.utils

object InputPreprocessUtils {
    fun searchQueryFilter(query: String): String {
        val rgx = Regex("[^a-zA-Z0-9 ]")
        return rgx.replace(query, "")
    }
}