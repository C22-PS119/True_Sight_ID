package com.truesightid.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.room.LocalDataSource

class ExploreNewsViewModel(private val mTrueSightRepository: LocalDataSource) : ViewModel() {
    fun getClaims(): LiveData<List<ClaimEntity>> = mTrueSightRepository.getClaims()
}