package com.owl.ainterview.ui.screens.session

import com.owl.domain.model.Question

enum class SessionStep {
    LOADING,        // Загрузка сессии из БД
    AI_SPEAKING,    // TTS читает вопрос
    LISTENING,      // STT слушает
    PROCESSING,     // Gemini оценивает
    FEEDBACK,       // Показываем результат
    COMPLETED       // Сессия завершена
}

data class SessionState(
    val step: SessionStep = SessionStep.LOADING,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val currentQuestion: Question? = null,

    // STT State
    val partialAnswer: String = "",
    val isMicEnabled: Boolean = false,

    // Feedback State
    val lastRating: Int = 0,
    val lastFeedback: String = ""
)

sealed interface SessionEffect {
    data object NavigateBack : SessionEffect
    data object NavigateToReport : SessionEffect
    data class ShowError(val message: String) : SessionEffect
}