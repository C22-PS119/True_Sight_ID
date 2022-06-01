package com.truesightid.data.source.local.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.truesightid.data.source.local.entity.ClaimEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalDataSource(
    private val trueSightDao: TrueSightDao,
    private val executor: ExecutorService
) {
    companion object {
        @Volatile
        private var instance: LocalDataSource? = null

        fun getInstance(context: Context): LocalDataSource {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TrueSightDatabase.getInstance(context)
                    instance = LocalDataSource(
                        database.truesightDao(),
                        Executors.newSingleThreadExecutor()
                    )
                }
                return instance as LocalDataSource
            }
        }
    }

    fun getClaims(): LiveData<List<ClaimEntity>> = trueSightDao.getAllClaims()

//    fun getNewsPrediction(predict: String): LiveData<NewsPredictionEntity> = trueSightDao.getNewsPrediction(predict)
//
//    fun updateNewsPrediction(news: NewsPredictionEntity) = trueSightDao.updateNewsPrediction(news)
}