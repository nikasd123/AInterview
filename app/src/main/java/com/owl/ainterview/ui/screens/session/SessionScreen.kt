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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.screens.session.compotents.AiSpeakingControls
import com.owl.ainterview.ui.screens.session.compotents.AudioVisualizer
import com.owl.ainterview.ui.screens.session.compotents.FeedbackCard
import com.owl.ainterview.ui.screens.session.compotents.ListeningControls
import com.owl.ainterview.ui.screens.session.compotents.QuestionCard
import com.owl.ainterview.ui.screens.session.compotents.SessionProgressBar
import com.owl.ainterview.ui.screens.session.compotents.SessionTopBar
import com.owl.ainterview.ui.theme.DarkBackground
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.Difficulty
import com.owl.domain.model.Question
import com.owl.domain.model.Topic
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
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    viewModel.collectSideEffect { effect ->
        when(effect) {
            is SessionEffect.NavigateBack -> onBackClick()
            is SessionEffect.NavigateToReport -> onNavigateToReport(effect.sessionId)
            is SessionEffect.ShowError -> { /* Show Toast */ }
        }
    }

    SessionScreenContent(
        state = state,
        formattedTime = formattedTime,
        onBackClick = onBackClick,
        onEndClick = { onBackClick() },
        onSkipReading = { viewModel.onSkipReading() },
        onRepeatClicked = { viewModel.onRepeatClicked() },
        onStopRecording = { viewModel.onStopRecording() },
        onCancelRecording = { viewModel.onCancelRecording() },
        onStartRecording = { viewModel.onStartRecording() },
        onNextQuestion = { viewModel.onNextQuestion() }
    )
}

@Composable
fun SessionScreenContent(
    state: SessionState,
    formattedTime: String,
    onBackClick: () -> Unit,
    onEndClick: () -> Unit,
    onSkipReading: () -> Unit,
    onRepeatClicked: () -> Unit,
    onStopRecording: () -> Unit,
    onCancelRecording: () -> Unit,
    onStartRecording: () -> Unit,
    onNextQuestion: () -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            SessionTopBar(
                topicTitle = state.currentQuestion?.topic?.displayName ?: "Interview",
                timer = formattedTime,
                onBackClick = onBackClick,
                onEndClick = onEndClick
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
            SessionProgressBar(state.currentQuestionIndex + 1, state.totalQuestions)

            Spacer(modifier = Modifier.height(24.dp))

            QuestionCard(
                text = state.currentQuestion?.text ?: "Loading...",
                isSpeaking = state.step == SessionStep.AI_SPEAKING
            )

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = state.step,
                label = "session_step",
                transitionSpec = {
                    fadeIn() + slideInVertically { it / 2 } togetherWith fadeOut()
                }
            ) { step ->
                when (step) {
                    SessionStep.AI_SPEAKING -> AiSpeakingControls(
                        onSkip = onSkipReading,
                        onRepeat = onRepeatClicked
                    )
                    SessionStep.LISTENING -> ListeningControls(
                        partialText = state.partialAnswer,
                        isMicActive = state.isMicEnabled,
                        onStopClick = onStopRecording,
                        onCancelClick = onCancelRecording,
                        onStartClick = onStartRecording
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
                        score = state.lastRating * 10,
                        feedback = state.lastFeedback,
                        onNextClick = onNextQuestion
                    )
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(name = "Listening State", showBackground = true)
@Composable
fun SessionScreenListeningPreview() {
    val mockQuestion = Question(
        id = "1",
        text = "Explain the difference between val and var in Kotlin.",
        topic = Topic.KOTLIN,
        difficulty = Difficulty.JUNIOR
    )

    val mockState = SessionState(
        step = SessionStep.LISTENING,
        currentQuestionIndex = 0,
        totalQuestions = 5,
        currentQuestion = mockQuestion,
        partialAnswer = "Val is immutable variable and...",
        isMicEnabled = true,
        elapsedSeconds = 45
    )

    SessionScreenContent(
        state = mockState,
        formattedTime = "00:45",
        onBackClick = {},
        onEndClick = {},
        onSkipReading = {},
        onRepeatClicked = {},
        onStopRecording = {},
        onCancelRecording = {},
        onStartRecording = {},
        onNextQuestion = {}
    )
}

@Preview(name = "Feedback State", showBackground = true)
@Composable
fun SessionScreenFeedbackPreview() {
    val mockQuestion = Question(
        id = "1",
        text = "Explain the difference between val and var in Kotlin.",
        topic = Topic.KOTLIN,
        difficulty = Difficulty.JUNIOR
    )

    val mockState = SessionState(
        step = SessionStep.FEEDBACK,
        currentQuestionIndex = 0,
        totalQuestions = 5,
        currentQuestion = mockQuestion,
        lastRating = 8,
        lastFeedback = "Correct! Val is immutable (read-only), while var is mutable. Ideally, mention that val is not necessarily constant (custom getters).",
        elapsedSeconds = 120
    )

    SessionScreenContent(
        state = mockState,
        formattedTime = "02:00",
        onBackClick = {},
        onEndClick = {},
        onSkipReading = {},
        onRepeatClicked = {},
        onStopRecording = {},
        onCancelRecording = {},
        onStartRecording = {},
        onNextQuestion = {}
    )
}