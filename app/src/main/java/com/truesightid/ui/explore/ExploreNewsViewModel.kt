package com.truesightid.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExploreNewsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is explore news Fragment"
    }
    val text: LiveData<String> = _text
}