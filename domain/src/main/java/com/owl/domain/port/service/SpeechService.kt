package com.owl.domain.port.service

import com.owl.domain.model.SpeechState
import kotlinx.coroutines.flow.StateFlow

interface SpeechService {
    val speechState: StateFlow<SpeechState>

    suspend fun startListening(language: String = "en-US")
    suspend fun stopListening()
    fun cleanup()
}