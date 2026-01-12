package com.owl.ainterview.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.owl.ainterview.R
import com.owl.ainterview.ui.screens.home.components.HomeHeader
import com.owl.ainterview.ui.screens.home.components.MainStatsCard
import com.owl.ainterview.ui.screens.home.components.SessionHistoryItem
import com.owl.ainterview.ui.screens.home.components.SmallStatCard
import com.owl.ainterview.ui.theme.AIInterviewerTheme
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.PlaceholderGray
import com.owl.ainterview.ui.theme.StatBlue
import com.owl.ainterview.ui.theme.StatPurple
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.Difficulty
import com.owl.domain.model.InterviewSession
import com.owl.domain.model.InterviewSettings
import com.owl.domain.model.Topic
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    onNavigateToSetup: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToReport: (String) -> Unit,
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            HomeEffect.NavigateToSetup -> onNavigateToSetup()
            HomeEffect.NavigateToSettings -> onNavigateToSettings()
            is HomeEffect.NavigateToReport -> onNavigateToReport(effect.sessionId)
        }
    }

    HomeScreenContent(
        state = state,
        onNewInterviewClick = { viewModel.onNewInterviewClick() },
        onSettingsClick = { viewModel.onSettingsClick() },
        onSessionClick = { sessionId -> viewModel.onSessionClick(sessionId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeState,
    onNewInterviewClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSessionClick: (String) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = Dimens.PaddingStandard)
                            .size(Dimens.ProfileSize)
                            .clip(CircleShape)
                            .background(PlaceholderGray)
                            .border(Dimens.ProfileBorder, NeonGreen, CircleShape)
                            .clickable { /* Обработка клика по профилю */ }
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = stringResource(R.string.nav_notifications),
                            tint = TextPrimary
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.nav_settings),
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewInterviewClick,
                containerColor = NeonGreen,
                contentColor = Color.Black,
                shape = RoundedCornerShape(Dimens.CornerRound)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                Text(stringResource(R.string.new_interview), fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(Dimens.PaddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge)
        ) {
            item {
                HomeHeader()
            }

            item {
                MainStatsCard(averageScore = state.averageScore)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimens.PaddingStandard
                    )
                ) {
                    SmallStatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.stat_completed),
                        value = "${state.completedCount}",
                        subtext = stringResource(R.string.stat_total_sessions),
                        icon = Icons.Default.CheckCircle,
                        color = StatBlue
                    )
                    SmallStatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.stat_top_skill),
                        value = state.topSkill,
                        subtext = stringResource(R.string.stat_most_practiced),
                        icon = Icons.Default.Star,
                        color = StatPurple
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.recent_sessions),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.TextSizeTitle
                    )
                    TextButton(onClick = {}) {
                        Text(
                            stringResource(R.string.view_all),
                            color = NeonGreen,
                            fontSize = Dimens.TextSizeSmall
                        )
                    }
                }
            }

            if (state.sessions.isEmpty() && !state.isLoading) {
                item {
                    Text(
                        stringResource(R.string.no_sessions_placeholder),
                        color = TextSecondary,
                        modifier = Modifier.padding(top = Dimens.PaddingStandard)
                    )
                }
            } else {
                items(state.sessions) { session ->
                    SessionHistoryItem(
                        session = session,
                        onClick = { onSessionClick(session.id) }
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
                }
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockSessions = listOf(
        InterviewSession(
            id = "1",
            date = LocalDateTime.now(),
            settings = InterviewSettings(
                topic = Topic.ANDROID,
                difficulty = Difficulty.MIDDLE,
                questionCount = 5
            ),
            questions = emptyList(),
            averageScore = 85,
            isFinished = true
        ),
        InterviewSession(
            id = "2",
            date = LocalDateTime.now().minusDays(1),
            settings = InterviewSettings(
                topic = Topic.KOTLIN,
                difficulty = Difficulty.JUNIOR,
                questionCount = 3
            ),
            questions = emptyList(),
            averageScore = 62,
            isFinished = true
        )
    )

    val mockState = HomeState(
        isLoading = false,
        sessions = mockSessions,
        averageScore = 78,
        completedCount = 12,
        topSkill = "System Design"
    )

    AIInterviewerTheme {
        HomeScreenContent(
            state = mockState,
            onNewInterviewClick = {},
            onSessionClick = {},
            onSettingsClick = {}
        )
    }
}