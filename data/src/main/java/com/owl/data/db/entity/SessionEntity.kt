package com.owl.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Question
import java.time.LocalDateTime

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val date: LocalDateTime,
    val settings: InterviewSettings,
    val questions: List<Question>,
    val averageScore: Int,
    val isFinished: Boolean
) {
    companion object {
        fun fromDomain(domain: InterviewSession): SessionEntity {
            return SessionEntity(
                id = domain.id,
                date = domain.date,
                settings = domain.settings,
                questions = domain.questions,
                averageScore = domain.averageScore,
                isFinished = domain.isFinished
            )
        }
    }

    fun toDomain(): InterviewSession {
        return InterviewSession(
            id = id,
            date = date,
            settings = settings,
            questions = questions,
            averageScore = averageScore,
            isFinished = isFinished
        )
    }
}