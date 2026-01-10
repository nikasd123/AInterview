package com.owl.domain.usecase

import com.owl.domain.common.Resource
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.InterviewSettings
import com.owl.domain.port.repository.SessionRepository
import com.owl.domain.port.service.AiInterviewerService
import com.owl.domain.usecase.language.GetCurrentLanguageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerateQuestionsUseCase(
    private val aiService: AiInterviewerService,
    private val sessionRepository: SessionRepository,
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase
) {
    suspend operator fun invoke(settings: InterviewSettings): Resource<InterviewSession> = withContext(
        Dispatchers.Default) {
        val language = getCurrentLanguageUseCase()
        when (val result = aiService.generateQuestions(settings, language)) {
            is Resource.Success -> {
                val session = InterviewSession(
                    settings = settings,
                    questions = result.data
                )

                sessionRepository.saveSession(session)
                Resource.Success(session)
            }
            is Resource.Error -> {
                Resource.Error(result.message, result.cause)
            }
            Resource.Loading -> Resource.Loading
        }
    }
}