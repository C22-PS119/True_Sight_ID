package com.truesightid.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truesightid.data.TrueSightRepository
import com.truesightid.ui.explore.ExploreNewsViewModel
import com.truesightid.ui.login.LoginViewModel
import com.truesightid.ui.main.MainViewModel
import com.truesightid.ui.prediction.NewsPredictViewModel
import com.truesightid.ui.profile.ProfileViewModel
import com.truesightid.utils.Injection

class ViewModelFactory private constructor(
    private val mTrueSightRepository: TrueSightRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            this.instance ?: synchronized(this) {
                ViewModelFactory(Injection.provideRepository(context)).apply {
                    this@Companion.instance = this
                }

            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(mTrueSightRepository) as T
            }
            modelClass.isAssignableFrom(ExploreNewsViewModel::class.java) -> {
                ExploreNewsViewModel(mTrueSightRepository) as T
            }
            modelClass.isAssignableFrom(NewsPredictViewModel::class.java) -> {
                NewsPredictViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(mTrueSightRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(mTrueSightRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}