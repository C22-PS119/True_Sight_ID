package com.truesightid.ui.language

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class LanguageManager(private val ct: Context) {
    private val sharedPreferences: SharedPreferences =
        ct.getSharedPreferences("LANG", Context.MODE_PRIVATE)

    fun updateResource(code: String) {
        val locale = Locale(code)
        Locale.setDefault(locale)
        val resources = ct.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        setLang(code)
    }

    fun getLang(): String? {
        return sharedPreferences.getString("lang", "en")
    }

    fun setLang(code: String) {
        val editor = sharedPreferences.edit()
        editor.putString("lang", code)
        editor.apply()
    }

}