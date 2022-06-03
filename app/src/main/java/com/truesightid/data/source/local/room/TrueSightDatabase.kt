package com.truesightid.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.truesightid.data.source.local.entity.ClaimEntity

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
                ).build().apply {
                    INSTANCE = this
                }
            }
    }


}