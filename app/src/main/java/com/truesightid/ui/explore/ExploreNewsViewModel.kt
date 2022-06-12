package com.truesightid.ui.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.GetClaimsRequest
import com.truesightid.utils.FilterSearch
import com.truesightid.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class ExploreNewsViewModel(private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun getClaims(request: GetClaimsRequest, onFinished: (success:Boolean, claims:LiveData<Resource<PagedList<ClaimEntity>>>?) -> Unit) {
        mTrueSightRepository.getDeletedClaims(request.apiKey){ success, _ ->
            if (success){
                GlobalScope.launch(Dispatchers.Main) {
                    onFinished(true, mTrueSightRepository.getAllClaims(request, null))
                }
            }else{
                onFinished(false, mTrueSightRepository.getAllClaims(request, null))
            }
        }
    }

    fun getClaimsWithFilter(request: GetClaimsRequest, filter: FilterSearch, onFinished: (success:Boolean, claims:LiveData<Resource<PagedList<ClaimEntity>>>?) -> Unit){
        mTrueSightRepository.getDeletedClaims(request.apiKey){ success, _ ->
            if (success){
                GlobalScope.launch(Dispatchers.Main) {
                    onFinished(true, mTrueSightRepository.getAllClaims(request, filter))
                }
            }else{
                onFinished(false, mTrueSightRepository.getAllClaims(request, filter))
            }
        }
    }

    fun upvoteClaimById(api_key: String, id: Int) =
        mTrueSightRepository.upVoteClaimById(api_key, id)

    fun downvoteClaimById(api_key: String, id: Int) =
        mTrueSightRepository.downVoteClaimById(api_key, id)

    fun addBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.addBookmarkById(request)

    fun removeBookmarkById(request: AddRemoveBookmarkRequest) =
        mTrueSightRepository.removeBookmarkById(request)

}