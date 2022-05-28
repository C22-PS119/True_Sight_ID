package com.truesightid.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.truesightid.data.TrueSightRepository
import com.truesightid.data.entity.ClaimEntity

class ExploreNewsViewModel(private val mTrueSightRepository: TrueSightRepository) : ViewModel() {
    fun getClaims(): LiveData<List<ClaimEntity>> = mTrueSightRepository.getClaims()
}