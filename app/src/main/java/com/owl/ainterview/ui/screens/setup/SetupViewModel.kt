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

    fun onTopicSelected(topic: Topic) = intent {
        reduce {
            state.copy(selectedTopic = topic)
        }
    }

    fun onDifficultySelected(difficulty: Difficulty) = intent {
        reduce {
            state.copy(selectedDifficulty = difficulty)
        }
    }

    fun onQuestionCountChanged(count: Int) = intent {
        reduce {
            state.copy(questionCount = count)
        }
    }

    fun onStartClicked() = intent {
        reduce { state.copy(isLoading = true) }

        val settings = InterviewSettings(
            topic = state.selectedTopic,
            difficulty = state.selectedDifficulty,
            questionCount = state.questionCount
        )

        when (val result = generateQuestionsUseCase(settings)) {
            is Resource.Success -> {
                reduce { state.copy(isLoading = false) }
                postSideEffect(SetupEffect.NavigateToSession(result.data.id))
            }
            is Resource.Error -> {
                reduce { state.copy(isLoading = false) }
                postSideEffect(SetupEffect.ShowError(result.message))
            }
            Resource.Loading -> {}
        }
    }
}