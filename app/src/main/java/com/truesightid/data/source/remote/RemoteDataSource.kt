package com.truesightid.data.source.remote

import com.truesightid.data.source.remote.response.NewsPredictionResponse
import com.truesightid.utils.ApiHelper

class RemoteDataSource private constructor(private val apiHelper: ApiHelper) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: ApiHelper): RemoteDataSource = instance ?: synchronized(this) {
            RemoteDataSource(helper).apply { instance = this }
        }
    }
//
//    fun getNewsPrediction(): LiveData<ApiResponse<NewsPredictionResponse>> {
//        val resultPrediction = MutableLiveData<ApiResponse<NewsPredictionResponse>>()
//        apiHelper.loadNewsPrediction(object : NewsPredictionCallback {
//            override fun onNewsPredictionReceived(predictionResponse: NewsPredictionResponse) {
//                resultPrediction.value = ApiResponse.success(predictionResponse)
//            }
//
//        })
//        return resultPrediction
//    }

    interface NewsPredictionCallback {
        fun onNewsPredictionReceived(predictionResponse: NewsPredictionResponse)
    }
}