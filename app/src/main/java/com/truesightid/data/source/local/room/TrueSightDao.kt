package com.truesightid.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.truesightid.data.source.local.entity.ClaimEntity

@Dao
interface TrueSightDao {
    @RawQuery(observedEntities = [ClaimEntity::class])
    fun getClaims(query: SimpleSQLiteQuery): DataSource.Factory<Int, ClaimEntity>

    @Query("SELECT * FROM claim WHERE claim_id = :id LIMIT 1")
    fun getClaimById(id: Int): LiveData<ClaimEntity>

    @Query("SELECT * FROM claim")
    fun getLocalClaims(): List<ClaimEntity>

    @Query("UPDATE claim SET upvote = upvote+1 WHERE claim_id = :id")
    fun upvoteWithId(id: Int)

    @Query("UPDATE claim SET downvote = downvote+1 WHERE claim_id = :id")
    fun downvoteWithId(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllClaims(claimEntity: List<ClaimEntity>)

    @Query("DELETE FROM claim WHERE claim_id = :id")
    fun deleteClaimById(id: Int)

    @Query("DELETE FROM claim")
    fun deleteAllLocalClaims()
}