package com.truesightid.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truesightid.data.TrueSightRepository
import com.truesightid.ui.explore.ExploreNewsViewModel
import com.truesightid.ui.prediction.NewsPredictViewModel
import com.truesightid.ui.profile.ProfileViewModel

class ViewModelFactory private constructor(private val mTrueSightRepository: TrueSightRepository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    TrueSightRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ExploreNewsViewModel::class.java) -> {
                ExploreNewsViewModel(mTrueSightRepository) as T
            }

            modelClass.isAssignableFrom(NewsPredictViewModel::class.java) -> {
                NewsPredictViewModel(mTrueSightRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(mTrueSightRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}