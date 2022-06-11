package com.truesightid.utils

import android.annotation.SuppressLint
import android.content.Context
import com.truesightid.R
import java.sql.Timestamp
import java.time.*
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

    private fun timeToSeconds(days: Int, hour: Int, minutes: Int, second: Int): Long{
        return (second + minutes * 60 + hour * 60 * 60 + days * 60 * 60 * 24).toLong()
    }


    fun getDateAsTimeAgo(seconds: Long, context: Context): String {
        val timeNow = Timestamp.from(LocalDate.now().atTime(LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant()).time / 1000L
//        Log.d("LOG TIME", seconds.toString())
//        Log.d("LOG TIME NOW", timeNow.toString())
        if (timeNow - seconds > timeToSeconds(0, 23, 59, 59)){
            return getDateTime(seconds).toString()
        }else if (timeNow - seconds > timeToSeconds(0, 0, 59, 59)){
            return ((timeNow - seconds) / 60 / 60).toString() + " " + context.getString(R.string.hour_ago)
        }else if (timeNow - seconds > timeToSeconds(0, 0, 0, 59)) {
            return ((timeNow - seconds) / 60).toString() + " " + context.getString(R.string.minute_ago)
        }else{
            return (timeNow - seconds).toString() + " " +  context.getString(R.string.second_ago)
        }
    }
}