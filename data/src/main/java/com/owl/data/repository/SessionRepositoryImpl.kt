package com.owl.data.repository

import com.owl.data.db.dao.SessionDao
import com.owl.data.db.entity.SessionEntity
import com.owl.domain.model.InterviewSession
import com.owl.domain.port.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val dao: SessionDao
) : SessionRepository {

    override fun getAllSessions(): Flow<List<InterviewSession>> {
        return dao.getAllSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSession(id: String): InterviewSession? {
        return dao.getSessionById(id)?.toDomain()
    }

    override suspend fun saveSession(session: InterviewSession) {
        dao.insertSession(SessionEntity.fromDomain(session))
    }
}