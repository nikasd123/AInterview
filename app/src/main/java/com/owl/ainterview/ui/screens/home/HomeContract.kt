package com.owl.ainterview.ui.screens.home

import com.owl.domain.model.InterviewSession

data class HomeState(
    val isLoading: Boolean = true,
    val sessions: List<InterviewSession> = emptyList(),

    val averageScore: Int = 0,
    val completedCount: Int = 0,
    val topSkill: String = "-"
)

sealed interface HomeEffect {
    data object NavigateToSetup : HomeEffect
    data class NavigateToReport(val sessionId: String) : HomeEffect
}