package com.owl.ainterview.ui.screens.session

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.screens.session.compotents.AudioVisualizer
import com.owl.ainterview.ui.screens.session.compotents.SessionProgressBar
import com.owl.ainterview.ui.screens.session.compotents.SessionTopBar
import com.owl.ainterview.ui.theme.DarkBackground
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary
import kotlinx.coroutines.Job
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SessionScreen(
    onBackClick: () -> Unit,
    onNavigateToReport: (String) -> Unit
) {
    val viewModel = koinViewModel<SessionViewModel>()
    val state by viewModel.collectAsState()

    val formattedTime = remember(state.elapsedSeconds) {
        val minutes = state.elapsedSeconds / 60
        val seconds = state.elapsedSeconds % 60
        "%02d:%02d".format(minutes, seconds)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { Unit }
    )

    // 2. Запрашиваем при старте экрана
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    viewModel.collectSideEffect { effect ->
        when(effect) {
            is SessionEffect.NavigateBack -> onBackClick()
            is SessionEffect.NavigateToReport -> onNavigateToReport(viewModel.sessionId)
            is SessionEffect.ShowError -> { /* Show Toast */ }
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            SessionTopBar(
                topicTitle = state.currentQuestion?.topic?.displayName ?: "Interview",
                timer = formattedTime,
                onBackClick = onBackClick,
                onEndClick = { onBackClick }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Progress
            SessionProgressBar(state.currentQuestionIndex + 1, state.totalQuestions)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Question Card (Всегда видна)
            QuestionCard(
                text = state.currentQuestion?.text ?: "Loading...",
                isSpeaking = state.step == SessionStep.AI_SPEAKING
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Dynamic Bottom Area (Меняется в зависимости от Step)
            AnimatedContent(
                targetState = state.step,
                label = "session_step",
                transitionSpec = {
                    fadeIn() + slideInVertically { it / 2 } togetherWith fadeOut()
                }
            ) { step ->
                when (step) {
                    SessionStep.AI_SPEAKING -> AiSpeakingControls(
                        onSkip = { viewModel.onSkipReading() },
                        onRepeat = { viewModel.onRepeatClicked() }
                    )
                    SessionStep.LISTENING -> ListeningControls(
                        partialText = state.partialAnswer,
                        isMicActive = state.isMicEnabled,
                        onStopClick = { viewModel.onStopRecording() },
                        onCancelClick = { viewModel.onCancelRecording() },
                        onStartClick = { viewModel.onStartRecording() }
                    )
                    SessionStep.PROCESSING -> Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonGreen)
                    }
                    SessionStep.FEEDBACK -> FeedbackCard(
                        score = state.lastRating * 10, // переводим 1-10 в 10-100%
                        feedback = state.lastFeedback,
                        onNextClick = { viewModel.onNextQuestion() }
                    )
                    else -> {} // Loading or Completed
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

// --- Sub-components for Layout ---

@Composable
fun QuestionCard(text: String, isSpeaking: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Badge
            if (isSpeaking) {
                Row(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AudioVisualizer(isAnimating = true, modifier = Modifier.size(20.dp, 12.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Speaking", color = TextSecondary, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Text
            Text(
                text = text,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Consider reliability, ordering, and connection overhead in your answer.",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AiSpeakingControls(
    onSkip: () -> Unit,
    onRepeat: () -> Unit // Добавили коллбэк
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onRepeat) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextSecondary)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Repeat", color = TextSecondary)
        }

        Button(
            onClick = onSkip,
            colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(50)
        ) {
            Text("Skip Reading", color = NeonGreen)
        }
    }
}

@Composable
fun ListeningControls(
    partialText: String,
    isMicActive: Boolean,
    onStopClick: () -> Unit,
    onCancelClick: () -> Unit,
    onStartClick: () -> Unit
) {
    // Анимация работает только если микрофон активен
    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")

    val pulseScale by if (isMicActive) {
        infiniteTransition.animateFloat(
            initialValue = 1f, targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "scale"
        )
    } else {
        remember { mutableFloatStateOf(1f) } // Без анимации
    }

    val pulseAlpha by if (isMicActive) {
        infiniteTransition.animateFloat(
            initialValue = 0.3f, targetValue = 0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "alpha"
        )
    } else {
        remember { mutableFloatStateOf(0f) } // Без свечения
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Меняем заголовок в зависимости от статуса
        Text(
            text = if (isMicActive) "LISTENING" else "MIC PAUSED",
            color = if (isMicActive) NeonGreen else TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isMicActive) "Go ahead..." else "Tap mic to resume",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text Area
        Text(
            text = when {
                !isMicActive && partialText.isBlank() -> "Recording stopped. Tap the mic."
                partialText.isBlank() -> "Listening for your voice..."
                else -> "\"$partialText\""
            },
            color = TextSecondary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .heightIn(min = 60.dp),
            lineHeight = 22.sp,
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- SMART BUTTON ---
        Box(contentAlignment = Alignment.Center) {
            // Glow (виден только если isMicActive)
            if (isMicActive) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(pulseScale)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(NeonGreen.copy(alpha = pulseAlpha), Color.Transparent)
                            )
                        )
                )
            }

            Button(
                onClick = {
                    if (isMicActive) onStopClick() else onStartClick()
                },
                modifier = Modifier
                    .size(72.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape, spotColor = NeonGreen),
                shape = CircleShape,
                // Если не активен — кнопка серая, если активен — зеленая
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isMicActive) NeonGreen else DarkSurface
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    // Меняем иконку: Микрофон (чтобы включить) или Стоп/Галочка (чтобы отправить)
                    imageVector = if (isMicActive) Icons.Default.Check else Icons.Default.Call,
                    contentDescription = null,
                    tint = if (isMicActive) Color.Black else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопки снизу
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onCancelClick,
                modifier = Modifier.background(DarkSurface, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cancel", tint = TextSecondary)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onStopClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Stop & Submit", color = Color.Black)
            }
        }
    }
}

@Composable
fun FeedbackCard(score: Int, feedback: String, onNextClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Strong Answer", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Key concepts covered", color = TextSecondary, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(4.dp, NeonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$score%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = feedback,
                color = TextSecondary,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Next Question", color = Color.Black)
            }
        }
    }
}