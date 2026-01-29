package com.owl.ainterview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owl.domain.controller.LanguageController
import kotlinx.coroutines.launch

class MainViewModel(
    private val languageController: LanguageController
) : ViewModel() {

    init {
        viewModelScope.launch {
            languageController.synchronize()
        }
    }
}