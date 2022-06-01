package com.truesightid.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.truesightid.data.source.local.entity.ClaimEntity

@Dao
interface TrueSightDao {
    @Query("SELECT * FROM claim")
    fun getAllClaims(): LiveData<List<ClaimEntity>>

    @Query("SELECT * FROM claim WHERE claim_id = :id LIMIT 1")
    fun getClaimById(id: Int): LiveData<ClaimEntity>

//    @Query("SELECT * FROM newsprediction")
//    fun getNewsPrediction(predict: String): LiveData<NewsPredictionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllClaims(vararg claimEntity: ClaimEntity)

    @Query("UPDATE claim SET upvote = upvote+1 WHERE claim_id = :id")
    fun upvoteWithId(id: Int)

    @Query("UPDATE claim SET downvote = downvote+1 WHERE claim_id = :id")
    fun downvoteWithId(id: Int)

//    @Update
//    fun updateNewsPrediction(news: NewsPredictionEntity)
}