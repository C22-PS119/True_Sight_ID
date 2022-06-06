package com.truesightid.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    @SuppressLint("SimpleDateFormat")
    fun getDateTime(time: Long): String? {
        return try {
            //val sdf = SimpleDateFormat("MM/dd/yyyy")
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val netDate = Date(time * 1000L)
            //sdf.format(netDate)
            netDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
            // ERROR TAPI BISA, ABAIKAN AJA
        } catch (e: Exception) {
            e.toString()
        }
    }
}