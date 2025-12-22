package com.owl.ainterview.ui.screens.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.viewmodel.container
import com.owl.domain.port.repository.SessionRepository
import org.orbitmvi.orbit.ContainerHost

class ReportViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository
) : ViewModel(), ContainerHost<ReportState, ReportEffect> {

    override val container = container<ReportState, ReportEffect>(ReportState())
    private val sessionId: String = checkNotNull(savedStateHandle["sessionId"])

    init {
        loadSession()
    }

    private fun loadSession() = intent {
        val session = sessionRepository.getSession(sessionId)
        reduce {
            state.copy(isLoading = false, session = session)
        }
    }

    fun toggleQuestionExpansion(questionId: String) = intent {
        reduce {
            val current = state.expandedQuestionIds
            val newSet = if (current.contains(questionId)) {
                current - questionId
            } else {
                current + questionId
            }
            state.copy(expandedQuestionIds = newSet)
        }
    }

    fun onFinishClick() = intent {
        postSideEffect(ReportEffect.NavigateHome)
    }
}