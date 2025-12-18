package com.owl.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class InterviewSession(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDateTime = LocalDateTime.now(),
    val settings: InterviewSettings,
    val questions: List<Question>,
    val averageScore: Int = 0,
    val isFinished: Boolean = false
)