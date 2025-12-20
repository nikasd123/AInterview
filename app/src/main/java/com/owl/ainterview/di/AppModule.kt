package com.owl.ainterview.di

import com.owl.ainterview.ui.screens.setup.SetupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SetupViewModel(generateQuestionsUseCase = get()) }
}