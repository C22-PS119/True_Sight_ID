package com.truesightid.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.truesightid.data.source.local.entity.ClaimEntity

@Dao
interface TrueSightDao {
    @Query("SELECT * FROM claim")
    fun getAllClaims(): DataSource.Factory<Int, ClaimEntity>

    @Query("SELECT * FROM claim WHERE claim_id = :id LIMIT 1")
    fun getClaimById(id: Int): LiveData<ClaimEntity>

    @Query("UPDATE claim SET upvote = upvote+1 WHERE claim_id = :id")
    fun upvoteWithId(id: Int)

    @Query("UPDATE claim SET downvote = downvote+1 WHERE claim_id = :id")
    fun downvoteWithId(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllClaims(claimEntity: List<ClaimEntity>)
}