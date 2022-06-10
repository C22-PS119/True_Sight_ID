package com.truesightid.utils

data class FilterSearch (
    val sortBy: Int,
    val type: Int,
    val optDate: Int,
    val dateFrom: Long?,
    val dateTo: Long?,
)