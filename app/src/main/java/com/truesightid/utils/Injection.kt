package com.truesightid.utils

import android.content.Context
import com.truesightid.api.ApiHelper
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.room.LocalDataSource
import com.truesightid.data.source.local.room.TrueSightDatabase
import com.truesightid.data.source.remote.RemoteDataSource


object Injection {
    fun provideRepository(context: Context): TrueSightRepository {

        val database = TrueSightDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(ApiHelper(context))
        val localDataSource = LocalDataSource.getInstance(database.truesightDao())
        val appExecutors = AppExecutors()

        return TrueSightRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}