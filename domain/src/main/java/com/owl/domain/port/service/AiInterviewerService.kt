package com.owl.domain.port.service

import com.owl.domain.common.Resource
import com.owl.domain.model.AppLanguage
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Question

interface AiInterviewerService {
    suspend fun generateQuestions(settings: InterviewSettings, language: AppLanguage): Resource<List<Question>>
    suspend fun evaluateAnswer(question: Question, answer: String, language: AppLanguage): Resource<Question>
}