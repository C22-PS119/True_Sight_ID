package com.truesightid.ui.prediction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truesightid.api.ApiConfig
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.GetClaimsRequest
import com.truesightid.data.source.remote.response.NewsPredictionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsPredictViewModel(val mTrueSightRepository: TrueSightRepository) : ViewModel() {

    private val _predictViewModel = MutableLiveData<NewsPredictionResponse?>()
    val predictViewModel: LiveData<NewsPredictionResponse?> = _predictViewModel

    companion object {
        const val TAG = "NewsPredictionViewModel"
    }

    fun getNewsPrediction(apiKey: String, predict: String, onComplete: (success: Boolean) -> Unit) {
        val client = ApiConfig.getApiService().getNewsPrediction(apiKey, predict)
        client.enqueue(object : Callback<NewsPredictionResponse> {
            override fun onResponse(
                call: Call<NewsPredictionResponse>,
                response: Response<NewsPredictionResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _predictViewModel.value = responseBody
                        onComplete(true)
                    }
                }
            }

            override fun onFailure(call: Call<NewsPredictionResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                onComplete(false)
            }
        })
    }

    fun getClaimsBySearch(request: GetClaimsRequest): LiveData<ApiResponse<List<ClaimEntity>>> =
        mTrueSightRepository.getClaimsBySearch(request)

    fun upvoteClaimById(api_key: String, id: Int) =
        mTrueSightRepository.upVoteClaimById(api_key, id)

    fun downvoteClaimById(api_key: String, id: Int) =
        mTrueSightRepository.downVoteClaimById(api_key, id)

    fun addBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.addBookmarkById(request)

    fun removeBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.removeBookmarkById(request)
}