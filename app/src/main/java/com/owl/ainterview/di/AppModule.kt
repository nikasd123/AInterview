package com.owl.ainterview.di

import com.owl.ainterview.ui.screens.home.HomeViewModel
import com.owl.ainterview.ui.screens.report.ReportViewModel
import com.owl.ainterview.ui.screens.session.SessionViewModel
import com.owl.ainterview.ui.screens.setup.SetupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SetupViewModel(generateQuestionsUseCase = get()) }
    viewModel { ReportViewModel(savedStateHandle = get(), sessionRepository = get()) }
    viewModel { HomeViewModel(getHomeDataUseCase = get()) }
    viewModel {
        SessionViewModel(
            savedStateHandle = get(),
            getSessionUseCase = get(),
            processAnswerUseCase = get(),
            speechService = get(),
            ttsService = get()
        )
    }
}