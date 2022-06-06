package com.truesightid.ui.prediction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truesightid.api.ApiConfig
import com.truesightid.data.source.remote.response.NewsPredictionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsPredictViewModel() : ViewModel() {

    private val _predictViewModel = MutableLiveData<NewsPredictionResponse?>()
    val predictViewModel: LiveData<NewsPredictionResponse?> = _predictViewModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        const val TAG = "NewsPredictionViewModel"
    }

    fun getNewsPrediction(apiKey: String, predict: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNewsPrediction(apiKey, predict)
        client.enqueue(object : Callback<NewsPredictionResponse> {
            override fun onResponse(
                call: Call<NewsPredictionResponse>,
                response: Response<NewsPredictionResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _predictViewModel.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<NewsPredictionResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }

        })
    }
}