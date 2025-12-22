package com.owl.ainterview.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owl.domain.port.repository.SessionRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel(), ContainerHost<HomeState, HomeEffect> {

    override val container = container<HomeState, HomeEffect>(HomeState())

    init {
        observeSessions()
    }

    private fun observeSessions() {
        viewModelScope.launch {
            sessionRepository.getAllSessions().collectLatest { sessions ->
                intent {
                    val completed = sessions.filter { it.isFinished }
                    val avgScore = if (completed.isNotEmpty()) {
                        completed.sumOf { it.averageScore } / completed.size
                    } else 0

                    // Находим самую частую тему
                    val topSkill = completed
                        .groupBy { it.settings.topic }
                        .maxByOrNull { it.value.size }
                        ?.key?.displayName ?: "None"

                    reduce {
                        state.copy(
                            isLoading = false,
                            sessions = sessions,
                            averageScore = avgScore,
                            completedCount = completed.size,
                            topSkill = topSkill
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