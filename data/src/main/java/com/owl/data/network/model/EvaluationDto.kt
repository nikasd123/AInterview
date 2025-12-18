package com.owl.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationDto(
    val rating: Int,
    val feedback: String,
    val idealAnswer: String
)