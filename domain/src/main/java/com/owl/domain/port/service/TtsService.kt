package com.owl.domain.port.service

import kotlinx.coroutines.flow.StateFlow

interface TtsService {
    val isSpeaking: StateFlow<Boolean>

    suspend fun speak(text: String)
    fun stop()
    fun shutdown()
}