package com.truesightid.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @SuppressLint("SimpleDateFormat")
    fun getDateTime(time: Long): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(time * 1000L)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}