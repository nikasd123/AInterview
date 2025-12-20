package com.owl.data.network.model

import com.owl.domain.model.Difficulty
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Topic
import kotlinx.serialization.Serializable

@Serializable
data class InterviewSettingsDto(
    val topicName: String,
    val difficultyName: String,
    val questionCount: Int
)

fun InterviewSettings.toDto() = InterviewSettingsDto(
    topicName = topic.name,
    difficultyName = difficulty.name,
    questionCount = questionCount
)

fun InterviewSettingsDto.toDomain() = InterviewSettings(
    topic = Topic.valueOf(topicName),
    difficulty = Difficulty.valueOf(difficultyName),
    questionCount = questionCount
)