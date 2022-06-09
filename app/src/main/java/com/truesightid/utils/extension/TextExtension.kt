package com.truesightid.utils.extension

import java.lang.StrictMath.floor

fun getTotalWords(text:String):Int{
    return text.split(" ").count()
}

fun getTotalWithUnit(total:Int):String{
    return if (total > 999_999)
        (floor(total/1000_0.0)/100.0).toString() + "M"
    else if (total > 999)
        (floor(total/10.0)/100.0).toString() + "K"
    else
        total.toString()
}

fun getTotalWithUnit(total:Long):String{
    return if (total > 999_999_999)
        (floor(total/1000_000_0.0)/100.0).toString() + "B"
    else if (total > 999_999)
        (floor(total/1000_0.0)/100.0).toString() + "M"
    else if (total > 999)
        (floor(total/10.0)/100.0).toString() + "K"
    else
        total.toString()
}
