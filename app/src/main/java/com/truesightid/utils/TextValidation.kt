package com.truesightid.utils

object TextValidation {
    fun getTotalWords(text:String):Int{
        return text.split(" ").count()
    }
}