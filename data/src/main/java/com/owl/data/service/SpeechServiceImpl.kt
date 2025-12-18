package com.owl.data.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.owl.domain.model.SpeechState
import com.owl.domain.port.service.SpeechService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SpeechServiceImpl(
    private val context: Context
) : SpeechService {

    private val _speechState = MutableStateFlow<SpeechState>(SpeechState.Idle)
    override val speechState: StateFlow<SpeechState> = _speechState.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
    }

    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _speechState.value = SpeechState.Listening
        }

        override fun onBeginningOfSpeech() {
            _speechState.value = SpeechState.Speaking("")
        }

        override fun onRmsChanged(rmsdB: Float) {
            // Здесь можно обновлять Flow громкости для визуалайзера
        }

        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {
            // Юзер замолчал, ждем обработки
            // Можно перевести в Loading
        }

        override fun onError(error: Int) {
            val errorMessage = mapError(error)
            _speechState.value = SpeechState.Error(errorMessage)

            stopListeningInternal()
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val text = matches?.firstOrNull()

            if (!text.isNullOrBlank()) {
                _speechState.value = SpeechState.Result(text)
            } else {
                _speechState.value = SpeechState.Error("No speech detected")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val text = matches?.firstOrNull()
            if (!text.isNullOrBlank()) {
                _speechState.value = SpeechState.Speaking(text)
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    override suspend fun startListening(languageCode: String) {
        withContext(Dispatchers.Main) {
            if (speechRecognizer == null) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                speechRecognizer?.setRecognitionListener(recognitionListener)
            }

            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)

            try {
                speechRecognizer?.startListening(speechIntent)
            } catch (e: Exception) {
                _speechState.value = SpeechState.Error("Start failed: ${e.message}")
            }
        }
    }

    override suspend fun stopListening() {
        withContext(Dispatchers.Main) {
            stopListeningInternal()
        }
    }

    private fun stopListeningInternal() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            Log.e("SpeechRecognizer", "$e")
        }
    }

    override fun cleanup() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    private fun mapError(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "Error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error $errorCode"
        }
    }
}