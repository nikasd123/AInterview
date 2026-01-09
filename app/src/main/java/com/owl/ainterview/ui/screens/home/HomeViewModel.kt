package com.owl.ainterview.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owl.domain.usecase.GetHomeDataUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel(), ContainerHost<HomeState, HomeEffect> {

    override val container = container<HomeState, HomeEffect>(HomeState())

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            getHomeDataUseCase().collectLatest { data ->
                intent {
                    reduce {
                        state.copy(
                            isLoading = false,
                            sessions = data.sessions,
                            averageScore = data.averageScore,
                            completedCount = data.completedCount,
                            topSkill = data.topSkill
                        )
                    }
                }
            }
        }
    }

    fun onNewInterviewClick() = intent {
        postSideEffect(HomeEffect.NavigateToSetup)
    }

    fun onSessionClick(sessionId: String) = intent {
        postSideEffect(HomeEffect.NavigateToReport(sessionId))
    }
}