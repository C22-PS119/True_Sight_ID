package com.truesightid.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.utils.DataDummy
import org.json.JSONException
import java.util.concurrent.Executors

@Database(
    entities = [ClaimEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TrueSightDatabase : RoomDatabase() {
    abstract fun truesightDao(): TrueSightDao

    companion object {
        @Volatile
        private var INSTANCE: TrueSightDatabase? = null

        fun getInstance(context: Context): TrueSightDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TrueSightDatabase::class.java,
                    "true_sight.db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        fillWithStartingData()
                    }

                }).build().apply {
                    INSTANCE = this
                }
            }


        private fun fillWithStartingData() {
            Executors.newSingleThreadExecutor().execute {
                val claims = DataDummy.generateDummyClaims()

                try {
                    for (claim in claims) {
                        INSTANCE?.truesightDao()?.insertAllClaims(
                            ClaimEntity(
                                claim.id,
                                claim.title,
                                claim.claimer,
                                claim.description,
                                claim.image,
                                claim.fake,
                                claim.upvote,
                                claim.downvote,
                                claim.date,
                                claim.voteCount
                            )
                        )
                    }
                } catch (exception: JSONException) {
                    exception.printStackTrace()
                }
            }
        }
    }


}