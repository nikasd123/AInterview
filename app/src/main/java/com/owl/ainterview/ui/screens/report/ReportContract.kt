package com.owl.ainterview.ui.screens.report

import com.owl.domain.model.InterviewSession

data class ReportState(
    val isLoading: Boolean = true,
    val session: InterviewSession? = null,
    val expandedQuestionIds: Set<String> = emptySet()
)

sealed interface ReportEffect {
    data object NavigateHome : ReportEffect
}