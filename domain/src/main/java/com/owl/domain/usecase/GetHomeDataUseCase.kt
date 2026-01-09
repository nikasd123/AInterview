package com.owl.domain.usecase

import com.owl.domain.model.HomeDashboardData
import com.owl.domain.port.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetHomeDataUseCase(
    private val repository: SessionRepository
) {
    operator fun invoke(): Flow<HomeDashboardData> {
        return repository.getAllSessions().map { sessions ->

            val completed = sessions.filter { it.isFinished }

            val avgScore = if (completed.isNotEmpty()) {
                completed.sumOf { it.averageScore } / completed.size
            } else 0

            val topSkill = completed
                .groupBy { it.settings.topic }
                .maxByOrNull { it.value.size }
                ?.key?.displayName ?: "-"

            HomeDashboardData(
                sessions = sessions.sortedByDescending { it.date },
                averageScore = avgScore,
                completedCount = completed.size,
                topSkill = topSkill
            )
        }
    }
}