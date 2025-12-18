package com.owl.domain.usecase

import com.owl.domain.common.Resource
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.InterviewSettings
import com.owl.domain.port.service.AiInterviewerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerateQuestionsUseCase(
    private val aiService: AiInterviewerService
) {
    suspend operator fun invoke(settings: InterviewSettings): Resource<InterviewSession> = withContext(
        Dispatchers.Default) {

        when (val result = aiService.generateQuestions(settings)) {
            is Resource.Success -> {
                val session = InterviewSession(
                    settings = settings,
                    questions = result.data
                )
                Resource.Success(session)
            }
            is Resource.Error -> {
                Resource.Error(result.message, result.cause)
            }
            Resource.Loading -> Resource.Loading
        }
    }
}