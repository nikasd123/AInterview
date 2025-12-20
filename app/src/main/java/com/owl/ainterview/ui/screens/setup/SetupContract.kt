package com.owl.ainterview.ui.screens.setup

import com.owl.domain.model.Difficulty
import com.owl.domain.model.Topic

data class SetupState(
    val selectedTopic: Topic = Topic.ANDROID,
    val selectedDifficulty: Difficulty = Difficulty.MIDDLE,
    val questionCount: Int = 3,
    val isLoading: Boolean = false,
    val availableTopics: List<Topic> = Topic.entries
)

sealed interface SetupEffect {
    data class NavigateToSession(val sessionId: String) : SetupEffect
    data class ShowError(val message: String) : SetupEffect
}