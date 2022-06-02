package com.truesightid.ui.addclaim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.room.LocalDataSource
import kotlinx.coroutines.launch

class AddClaimViewModel(private val mTrueSightRepository: LocalDataSource) : ViewModel() {
    fun addClaim(itemClaim: ClaimEntity) {
        viewModelScope.launch { mTrueSightRepository.addClaim(itemClaim) }
    }
}