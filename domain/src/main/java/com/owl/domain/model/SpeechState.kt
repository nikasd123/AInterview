package com.owl.domain.model

sealed interface SpeechState {
    data object Idle : SpeechState
    data object Listening : SpeechState
    data class Speaking(val partialText: String) : SpeechState
    data class Result(val text: String) : SpeechState
    data class Error(val message: String) : SpeechState
}