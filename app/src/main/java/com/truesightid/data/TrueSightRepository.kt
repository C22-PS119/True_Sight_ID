package com.truesightid.data

import com.truesightid.data.source.local.room.LocalDataSource
import com.truesightid.data.source.remote.RemoteDataSource
import com.truesightid.utils.AppExecutors

class TrueSightRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutor: AppExecutors
) : TrueSightDataSource {
    companion object {
        @Volatile
        private var instance: TrueSightRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): TrueSightRepository =
            instance ?: synchronized(this) {
                TrueSightRepository(remoteData, localData, appExecutors).apply {
                    instance = this
                }
            }
    }

//    override fun getAllClaims(): LiveData<Resource<List<ClaimEntity>>> {
//    }

//    override fun getNewsPrediction(): LiveData<Resource<NewsPredictionEntity>> {
//        return object :
//            NetworkBoundResource<NewsPredictionEntity, NewsPredictionResponse>(appExecutor) {
//            override fun loadFromDB(): LiveData<NewsPredictionEntity> =
//                localDataSource.getNewsPrediction(predict)
//
//            override fun shouldFetch(data: NewsPredictionEntity?): Boolean =
//                data != null && data.content.isEmpty()
//
//            override fun createCall(): LiveData<ApiResponse<NewsPredictionResponse>> =
//                remoteDataSource.getNewsPrediction()
//
//            override fun saveCallResult(data: NewsPredictionResponse) {
//                val response = data.data
//                val newsPrediction = NewsPredictionEntity(
//                    content = response?.claim as String,
//                    prediction = response.prediction as Float
//                )
//                localDataSource.updateNewsPrediction(newsPrediction)
//            }
//        }.asLiveData()
//    }
}