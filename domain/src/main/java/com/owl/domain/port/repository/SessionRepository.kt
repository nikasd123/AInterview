package com.owl.domain.port.repository

import com.owl.domain.model.InterviewSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getAllSessions(): Flow<List<InterviewSession>>

    suspend fun getSession(id: String): InterviewSession?
    suspend fun saveSession(session: InterviewSession)
}