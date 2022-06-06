package com.truesightid.utils

object VotesSeparator {
    fun separate(votes: String?): HashMap<Int, Int> {
        if (votes == null)
            return HashMap<Int, Int>()
        val result = HashMap<Int, Int>()
        val ans = votes.split(",")
        for (text in ans) {
            val split = text.split(":")
            if (split.count() > 0)
                result[split[0].toInt()] = split[1].toInt()
        }
        return result
    }
}