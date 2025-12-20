package com.owl.ainterview.ui.screens.setup

import androidx.lifecycle.ViewModel
import com.owl.domain.common.Resource
import com.owl.domain.model.Difficulty
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Topic
import com.owl.domain.usecase.GenerateQuestionsUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SetupViewModel(
    private val generateQuestionsUseCase: GenerateQuestionsUseCase
) : ViewModel(), ContainerHost<SetupState, SetupEffect> {

    override val container = container<SetupState, SetupEffect>(SetupState())

    // UI Event: Пользователь выбрал тему
    fun onTopicSelected(topic: Topic) = intent {
        reduce {
            state.copy(selectedTopic = topic)
        }
    }

    // UI Event: Пользователь выбрал сложность
    fun onDifficultySelected(difficulty: Difficulty) = intent {
        reduce {
            state.copy(selectedDifficulty = difficulty)
        }
    }

    // UI Event: Пользователь изменил кол-во вопросов
    fun onQuestionCountChanged(count: Int) = intent {
        reduce {
            state.copy(questionCount = count)
        }
    }

    // UI Event: Нажали кнопку "Start Interview"
    fun onStartClicked() = intent {
        // 1. Блокируем UI лоадером
        reduce { state.copy(isLoading = true) }

        // 2. Формируем настройки
        val settings = InterviewSettings(
            topic = state.selectedTopic,
            difficulty = state.selectedDifficulty,
            questionCount = state.questionCount
        )

        // 3. Вызываем UseCase (Gemini API)
        // Orbit сам обрабатывает корутины, нам не нужен viewModelScope.launch
        val result = generateQuestionsUseCase(settings)

        // 4. Обрабатываем результат
        when (result) {
            is Resource.Success -> {
                // Успех: убираем лоадер и навигируемся
                reduce { state.copy(isLoading = false) }
                // Передаем ID сессии (или саму сессию, если через shared ViewModel, но ID лучше)
                postSideEffect(SetupEffect.NavigateToSession(result.data.id))
            }
            is Resource.Error -> {
                // Ошибка: убираем лоадер и показываем тост
                reduce { state.copy(isLoading = false) }
                postSideEffect(SetupEffect.ShowError(result.message))
            }
            Resource.Loading -> {
                // Можно обработать, но мы уже выставили isLoading = true
            }
        }
    }
}