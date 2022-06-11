package com.truesightid.utils

import android.content.Context
import android.util.Log
import java.util.*
import com.truesightid.R

fun translateServerRespond(message: String, context: Context) : String {
    val locale = Locale("en")
    val resources = context.resources
    val configuration = resources.configuration
    val lastLocale = configuration.locale
    Locale.setDefault(locale)
    configuration.locale = locale
    resources.updateConfiguration(configuration, null)

    val en = resources.getStringArray(R.array.error_message)
    for (i in 0 until en.count()) {
        if (message.lowercase() == en[i].lowercase()){
            Locale.setDefault(lastLocale)
            configuration.locale = lastLocale
            resources.updateConfiguration(configuration, null)
            val translate = resources.getStringArray(R.array.error_message)
            return translate[i]
        }
    }
    Locale.setDefault(lastLocale)
    configuration.locale = lastLocale
    resources.updateConfiguration(configuration, null)
    return message
}