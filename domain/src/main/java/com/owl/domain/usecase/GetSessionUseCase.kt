package com.owl.domain.usecase

import com.owl.domain.model.InterviewSession
import com.owl.domain.port.repository.SessionRepository

class GetSessionUseCase(
    private val repository: SessionRepository
) {
    suspend operator fun invoke(sessionId: String): InterviewSession? {
        return repository.getSession(sessionId)
    }
}