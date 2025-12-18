package com.owl.domain.model

import java.util.UUID

data class Question(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val topic: Topic,
    val difficulty: Difficulty,

    val userAnswerAudioPath: String? = null,
    val userAnswerText: String? = null,
    val aiFeedback: String? = null,
    val rating: Int? = null, // Оценка 1-10
    val isCompleted: Boolean = false
)