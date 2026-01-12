package com.owl.ainterview.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.owl.ainterview.R
import com.owl.ainterview.ui.screens.report.components.FeedbackSummaryCard
import com.owl.ainterview.ui.screens.report.components.QuestionResultItem
import com.owl.ainterview.ui.screens.report.components.ScoreCircle
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.SuccessGreenDark
import com.owl.ainterview.ui.theme.SuccessText
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.domain.model.Difficulty
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Question
import com.owl.domain.model.Topic
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDateTime

@Composable
fun ReportScreen(
    onHomeClick: () -> Unit,
) {
    val viewModel = koinViewModel<ReportViewModel>()
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            ReportEffect.NavigateHome -> onHomeClick()
        }
    }

    ReportScreenContent(
        state = state,
        onFinishClick = { viewModel.onFinishClick() },
        onExpandClick = { questionId -> viewModel.toggleQuestionExpansion(questionId) }
    )
}

@Composable
fun ReportScreenContent(
    state: ReportState,
    onFinishClick: () -> Unit,
    onExpandClick: (String) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ReportTopBar(onBackClick = onFinishClick)
        },
        bottomBar = {
            Button(
                onClick = onFinishClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingStandard)
                    .height(Dimens.ButtonHeightStandard),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(Dimens.ButtonCornerRadius)
            ) {
                Text(
                    text = stringResource(R.string.finish_review),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        if (state.isLoading || state.session == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            val session = state.session

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    horizontal = Dimens.PaddingLarge,
                    vertical = Dimens.PaddingStandard
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.great_job),
                            color = TextPrimary,
                            fontSize = Dimens.TextSizeDisplay,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                        ScoreCircle(score = session.averageScore)

                        Spacer(modifier = Modifier.height(Dimens.PaddingStandard))

                        if (session.averageScore > 80) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        SuccessGreenDark,
                                        RoundedCornerShape(Dimens.CornerRound)
                                    )
                                    .padding(
                                        horizontal = Dimens.PaddingStandard,
                                        vertical = 6.dp
                                    )
                            ) {
                                Text(
                                    text = stringResource(R.string.top_10_percent),
                                    color = SuccessText,
                                    fontSize = Dimens.TextSizeSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    FeedbackSummaryCard(summary = "You demonstrated strong skills in ${session.settings.topic.displayName}. Keep practicing consistent hashing and edge cases.")
                }

                item {
                    Text(
                        text = stringResource(R.string.detailed_analysis),
                        color = TextPrimary,
                        fontSize = Dimens.TextSizeTitle,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(session.questions) { index, question ->
                    QuestionResultItem(
                        index = index + 1,
                        question = question,
                        isExpanded = state.expandedQuestionIds.contains(question.id),
                        onExpandClick = { onExpandClick(question.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(Dimens.SpacerLarge)) }
            }
        }
    }
}

@Composable
fun ReportTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingStandard, vertical = Dimens.PaddingStandard),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = TextPrimary
            )
        }
        Text(
            text = stringResource(R.string.report_title),
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = Dimens.TextSizeTitle
        )
        IconButton(onClick = { /* Share Logic */ }) {
            Icon(
                Icons.Default.Share,
                contentDescription = stringResource(R.string.cd_share),
                tint = TextPrimary
            )
        }
    }
}

@Preview
@Composable
fun ReportScreenPreview() {
    val q1 = Question(
        id = "1",
        text = "Explain the difference between TCP and UDP.",
        topic = Topic.ANDROID,
        difficulty = Difficulty.MIDDLE,
        rating = 9,
        userAnswerText = "TCP is reliable, UDP is faster but not guaranteed.",
        aiFeedback = "Correct. You mentioned the key difference: reliability.",
        isCompleted = true
    )

    val q2 = Question(
        id = "2",
        text = "What is a Memory Leak in Android?",
        topic = Topic.ANDROID,
        difficulty = Difficulty.MIDDLE,
        rating = 4,
        userAnswerText = "I don't know exactly.",
        aiFeedback = "A memory leak happens when an object is retained longer than needed.",
        isCompleted = true
    )

    val mockSession = InterviewSession(
        id = "test_session",
        date = LocalDateTime.now(),
        settings = InterviewSettings(Topic.ANDROID, Difficulty.MIDDLE, 2),
        questions = listOf(q1, q2),
        averageScore = 65,
        isFinished = true
    )

    val mockState = ReportState(
        isLoading = false,
        session = mockSession,
        expandedQuestionIds = setOf("2")
    )

    ReportScreenContent(
        state = mockState,
        onFinishClick = {},
        onExpandClick = {}
    )
}