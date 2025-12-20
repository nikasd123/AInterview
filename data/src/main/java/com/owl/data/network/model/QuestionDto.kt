package com.owl.data.network.model

import android.R.attr.rating
import com.owl.domain.model.Difficulty
import com.owl.domain.model.Question
import com.owl.domain.model.Topic
import kotlinx.coroutines.NonCancellable.isCompleted
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: String,
    val text: String,
    val topicName: String,
    val difficultyName: String,
    val userAnswerAudioPath: String?,
    val userAnswerText: String?,
    val aiFeedback: String?,
    val rating: Int?,
    val isCompleted: Boolean
)

fun Question.toDto() = QuestionDto(
    id = id,
    text = text,
    topicName = topic.name,
    difficultyName = difficulty.name,
    userAnswerAudioPath = userAnswerAudioPath,
    userAnswerText = userAnswerText,
    aiFeedback = aiFeedback,
    rating = rating,
    isCompleted = isCompleted
)

fun QuestionDto.toDomain() = Question(
    id = id,
    text = text,
    topic = Topic.valueOf(topicName),
    difficulty = Difficulty.valueOf(difficultyName),
    userAnswerAudioPath = userAnswerAudioPath,
    userAnswerText = userAnswerText,
    aiFeedback = aiFeedback,
    rating = rating,
    isCompleted = isCompleted
)