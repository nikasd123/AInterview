package com.owl.domain.usecase

import com.owl.domain.common.Resource
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.Question
import com.owl.domain.port.repository.SessionRepository
import com.owl.domain.port.service.AiInterviewerService

class ProcessAnswerUseCase(
    private val aiService: AiInterviewerService,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        session: InterviewSession,
        question: Question,
        userAnswer: String
    ): Resource<Pair<InterviewSession, Question>> {

        val result = aiService.evaluateAnswer(question, userAnswer)

        return when (result) {
            is Resource.Success -> {
                val evaluatedQuestion = result.data

                // 2. Обновляем список вопросов (Бизнес-логика)
                val updatedQuestions = session.questions.map {
                    if (it.id == question.id) evaluatedQuestion else it
                }

                // 3. Пересчитываем средний балл (Бизнес-логика)
                val completedQuestions = updatedQuestions.filter { it.isCompleted }
                val avgScore = if (completedQuestions.isNotEmpty()) {
                    completedQuestions.sumOf { it.rating ?: 0 } / completedQuestions.size
                } else 0

                // 4. Создаем новую версию сессии
                val updatedSession = session.copy(
                    questions = updatedQuestions,
                    averageScore = avgScore,
                    isFinished = updatedQuestions.all { it.isCompleted }
                )

                // 5. Сохраняем в БД
                sessionRepository.saveSession(updatedSession)

                Resource.Success(updatedSession to evaluatedQuestion)
            }
            is Resource.Error -> {
                Resource.Error(result.message, result.cause)
            }
            Resource.Loading -> Resource.Loading
        }
    }
}