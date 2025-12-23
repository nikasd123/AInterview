package com.owl.ainterview.ui.screens.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owl.domain.common.Resource
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.Question
import com.owl.domain.model.SpeechState
import com.owl.domain.port.repository.SessionRepository
import com.owl.domain.port.service.AiInterviewerService
import com.owl.domain.port.service.SpeechService
import com.owl.domain.port.service.TtsService
import com.owl.domain.usecase.GetSessionUseCase
import com.owl.domain.usecase.ProcessAnswerUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SessionViewModel(
    savedStateHandle: SavedStateHandle,
    private val getSessionUseCase: GetSessionUseCase,
    private val processAnswerUseCase: ProcessAnswerUseCase,
    private val speechService: SpeechService,
    private val ttsService: TtsService,
) : ViewModel(), ContainerHost<SessionState, SessionEffect> {

    override val container = container<SessionState, SessionEffect>(SessionState())

    internal val sessionId: String = checkNotNull(savedStateHandle["sessionId"])
    private var session: InterviewSession? = null

    init {
        loadSession()
        observeTts()
        observeSpeech()
    }

    // --- 1. Initialization ---

    private fun loadSession() = intent {
        // Обращаемся к UseCase вместо репозитория
        val loadedSession = getSessionUseCase(sessionId)

        if (loadedSession != null) {
            session = loadedSession
            reduce {
                state.copy(
                    step = SessionStep.AI_SPEAKING,
                    totalQuestions = loadedSession.questions.size,
                    currentQuestionIndex = 0, // Или логика поиска первого неотвеченного
                    currentQuestion = loadedSession.questions.firstOrNull { !it.isCompleted }
                        ?: loadedSession.questions.firstOrNull()
                )
            }
            speakCurrentQuestion()
        } else {
            postSideEffect(SessionEffect.ShowError("Session not found"))
            postSideEffect(SessionEffect.NavigateBack)
        }
    }

    // --- 2. TTS Logic (Robot Voice) ---

    private fun speakCurrentQuestion() = intent {
        state.currentQuestion?.text?.let { text ->
            ttsService.speak(text)
        }
    }

    private fun observeTts() {
        viewModelScope.launch {
            ttsService.isSpeaking.collectLatest { isSpeaking ->
                intent {
                    // Если робот закончил говорить и мы были в шаге AI_SPEAKING
                    if (!isSpeaking && state.step == SessionStep.AI_SPEAKING) {
                        // Автоматически включаем микрофон
                        startListening()
                    }
                }
            }
        }
    }

    fun onSkipReading() = intent {
        ttsService.stop()
        startListening()
    }

    // --- 3. STT Logic (User Voice) ---

    private fun startListening() = intent {
        reduce { state.copy(step = SessionStep.LISTENING, partialAnswer = "", isMicEnabled = true) }
        speechService.startListening()
    }

    fun onStopRecording() = intent {
        speechService.stopListening()
        // Ответ обработается в observeSpeech -> SpeechState.Result
    }

    private fun observeSpeech() {
        viewModelScope.launch {
            speechService.speechState.collectLatest { speechState ->
                intent {
                    if (state.step != SessionStep.LISTENING) return@intent

                    when (speechState) {
                        is SpeechState.Speaking -> {
                            reduce { state.copy(partialAnswer = speechState.partialText) }
                        }

                        is SpeechState.Result -> {
                            // Распознавание завершено -> Отправляем на проверку
                            submitAnswer(speechState.text)
                        }

                        is SpeechState.Error -> {
                            // Ошибка (например, тишина) -> Просим повторить или стопаем
                            postSideEffect(SessionEffect.ShowError(speechState.message))
                            // Для простоты можно остаться в LISTENING или дать кнопку Retry
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    // --- 4. AI Evaluation Logic ---

    private fun submitAnswer(answerText: String) = intent {
        val currentSess = session ?: return@intent
        val question = state.currentQuestion ?: return@intent

        reduce { state.copy(step = SessionStep.PROCESSING, partialAnswer = answerText) }

        val result = processAnswerUseCase(currentSess, question, answerText)

        when (result) {
            is Resource.Success -> {
                val (updatedSession, evaluatedQuestion) = result.data
                session = updatedSession

                reduce {
                    state.copy(
                        step = SessionStep.FEEDBACK,
                        lastRating = evaluatedQuestion.rating ?: 0,
                        lastFeedback = evaluatedQuestion.aiFeedback ?: ""
                    )
                }
            }

            is Resource.Error -> {
                reduce { state.copy(step = SessionStep.LISTENING) }
                postSideEffect(SessionEffect.ShowError(result.message))
            }

            else -> {}
        }
    }

    // --- 5. Navigation Logic ---

    fun onNextQuestion() = intent {
        val currentSess = session ?: return@intent
        val nextIndex = state.currentQuestionIndex + 1

        if (nextIndex < currentSess.questions.size) {
            val nextQuestion = currentSess.questions[nextIndex]
            reduce {
                state.copy(
                    step = SessionStep.AI_SPEAKING,
                    currentQuestionIndex = nextIndex,
                    currentQuestion = nextQuestion,
                    partialAnswer = ""
                )
            }
            speakCurrentQuestion()
        } else {
            reduce { state.copy(step = SessionStep.COMPLETED) }
            postSideEffect(SessionEffect.NavigateToReport(sessionId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsService.stop()
        speechService.cleanup()
    }
}