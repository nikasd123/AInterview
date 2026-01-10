package com.owl.domain.usecase

import com.owl.domain.common.Resource
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.Question
import com.owl.domain.port.repository.SessionRepository
import com.owl.domain.port.service.AiInterviewerService
import com.owl.domain.usecase.language.GetCurrentLanguageUseCase

class ProcessAnswerUseCase(
    private val aiService: AiInterviewerService,
    private val sessionRepository: SessionRepository,
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase
) {
    suspend operator fun invoke(
        session: InterviewSession,
        question: Question,
        userAnswer: String
    ): Resource<Pair<InterviewSession, Question>> {
        val language = getCurrentLanguageUseCase()
        val result = aiService.evaluateAnswer(question, userAnswer, language)

        return when (result) {
            is Resource.Success -> {
                val evaluatedQuestion = result.data

                val updatedQuestions = session.questions.map {
                    if (it.id == question.id) evaluatedQuestion else it
                }

                val completedQuestions = updatedQuestions.filter { it.isCompleted }
                val avgScore = if (completedQuestions.isNotEmpty()) {
                    completedQuestions.sumOf { it.rating ?: 0 } / completedQuestions.size
                } else 0

                val updatedSession = session.copy(
                    questions = updatedQuestions,
                    averageScore = avgScore,
                    isFinished = updatedQuestions.all { it.isCompleted }
                )

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