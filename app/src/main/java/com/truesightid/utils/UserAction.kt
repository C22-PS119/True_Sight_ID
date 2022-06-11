package com.truesightid.utils

object UserAction {
    fun applyUserVotes(claim_id: Int, vote: Int) {
        val user = Prefs.getUser()
        if (vote == 0)
            user?.votes?.remove(claim_id)
        else
            user?.votes!![claim_id] = vote
        Prefs.setUser(user)
    }

    fun applyUserBookmarks(claim_id: Int, bookmark: Boolean) {
        val user = Prefs.getUser()
        if (bookmark) {
            if (user?.bookmark?.contains(claim_id) != true)
                user?.bookmark?.add(claim_id)
        }else {
            if (user?.bookmark?.contains(claim_id) == true)
                user?.bookmark?.remove(claim_id)
        }
        Prefs.setUser(user)
    }
}