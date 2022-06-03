package com.truesightid.utils

object VotesSeparator {
    fun separate(votes: String): HashMap<Int, Int> {
        val result = HashMap<Int, Int>()
        val ans = votes.split(",")
        for (text in ans) {
            val split = text.split(":")
            result[split[0].toInt()] = split[1].toInt()
        }
        return result
    }
}