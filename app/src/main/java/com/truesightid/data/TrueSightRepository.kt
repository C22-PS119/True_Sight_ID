package com.truesightid.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.truesightid.data.entity.ClaimEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TrueSightRepository(
    private val trueSightDao: TrueSightDao,
    private val executor: ExecutorService
) {
    companion object {
        @Volatile
        private var instance: TrueSightRepository? = null

        fun getInstance(context: Context): TrueSightRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TrueSightDatabase.getInstance(context)
                    instance = TrueSightRepository(
                        database.truesightDao(),
                        Executors.newSingleThreadExecutor()
                    )
                }
                return instance as TrueSightRepository
            }
        }
    }

    fun getClaims(): LiveData<List<ClaimEntity>> = trueSightDao.getAllClaims()
}