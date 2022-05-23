package com.truesightid.ui.prediction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsPredictViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is news predict Fragment"
    }
    val text: LiveData<String> = _text
}