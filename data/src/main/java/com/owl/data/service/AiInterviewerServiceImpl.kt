package com.owl.data.service

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.owl.data.network.model.EvaluationDto
import com.owl.data.network.model.NetworkQuestionDto
import com.owl.data.network.model.QuestionDto
import com.owl.domain.common.Resource
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Question
import com.owl.domain.port.service.AiInterviewerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.collections.map

class AiInterviewerServiceImpl(
    private val apiKey: String
) : AiInterviewerService {

    // Настраиваем Gemini на JSON режим
    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
            }
        )
    }

    // JSON парсер, игнорирующий неизвестные поля (на всякий случай)
    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun generateQuestions(settings: InterviewSettings): Resource<List<Question>> =
        withContext(Dispatchers.IO) {
            try {
                // 1. Промпт остается тем же (обрати внимание на ключи topic/difficulty)
                val prompt = """
                    You are a strict Senior Android Developer conducting a technical interview.
                    Generate ${settings.questionCount} interview questions about "${settings.topic.displayName}".
                    Difficulty level: ${settings.difficulty.name}.
                    
                    Return the result ONLY as a JSON Array with this exact schema:
                    [
                      {
                        "text": "Question text here",
                        "topic": "${settings.topic.name}",
                        "difficulty": "${settings.difficulty.name}"
                      }
                    ]
                    Do not add any markdown formatting. Just raw JSON.
                """.trimIndent()

                val response = generativeModel.generateContent(prompt)
                val responseText = response.text ?: return@withContext Resource.Error("Empty response from AI")

                // 2. Парсим в ЛЕГКУЮ модель (NetworkQuestionDto)
                val dtos = jsonParser.decodeFromString<List<NetworkQuestionDto>>(responseText)

                // 3. Маппим в Domain Model и ДОБАВЛЯЕМ недостающие данные (ID, defaults)
                val questions = dtos.map { dto ->
                    Question(
                        id = java.util.UUID.randomUUID().toString(), // Генерируем ID здесь!
                        text = dto.text,
                        topic = settings.topic, // Берем из настроек или парсим dto.topic
                        difficulty = settings.difficulty,
                        // Остальные поля (userAnswer, rating...) Kotlin заполнит null'ами по умолчанию
                    )
                }

                Resource.Success(questions)

            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error("Failed to generate questions: ${e.localizedMessage}", e)
            }
        }

    override suspend fun evaluateAnswer(question: Question, answer: String): Resource<Question> =
        withContext(Dispatchers.IO) {
            try {
                val prompt = """
                    You are a Senior Android Interviewer.
                    Question: "${question.text}"
                    Topic: ${question.topic.displayName}
                    User Answer: "$answer"
                    
                    Evaluate the answer concisely. 
                    If the answer is empty or nonsense, give rating 1.
                    
                    Return ONLY JSON:
                    {
                      "rating": (integer 1-10),
                      "feedback": "Short constructive feedback (max 2 sentences)",
                      "idealAnswer": "A concise correct answer (max 2 sentences)"
                    }
                """.trimIndent()

                val response = generativeModel.generateContent(prompt)
                val responseText = response.text ?: return@withContext Resource.Error("Empty response from AI")

                val evaluation = jsonParser.decodeFromString<EvaluationDto>(responseText)

                // Возвращаем обновленный вопрос с результатами
                val ratedQuestion = question.copy(
                    userAnswerText = answer,
                    aiFeedback = evaluation.feedback + "\n\nIdeal: " + evaluation.idealAnswer,
                    rating = evaluation.rating,
                    isCompleted = true
                )

                Resource.Success(ratedQuestion)

            } catch (e: Exception) {
                Resource.Error("Failed to evaluate answer: ${e.message}", e)
            }
        }
}