package com.owl.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val text: String,
    val topic: String,
    val difficulty: String
)