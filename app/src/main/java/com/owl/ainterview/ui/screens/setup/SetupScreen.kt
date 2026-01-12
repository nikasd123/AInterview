package com.owl.ainterview.ui.screens.setup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.screens.setup.components.DifficultySelector
import com.owl.ainterview.ui.screens.setup.components.QuestionCountCard
import com.owl.ainterview.ui.screens.setup.components.SectionHeader
import com.owl.ainterview.ui.screens.setup.components.SetupTopBar
import com.owl.ainterview.ui.screens.setup.components.StartButton
import com.owl.ainterview.ui.screens.setup.components.TopicChip
import com.owl.ainterview.ui.theme.DarkBackground
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.Difficulty
import com.owl.domain.model.Topic
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SetupScreen(
    onBackClick: () -> Unit,
    onNavigateToSession: (String) -> Unit,
) {
    val viewModel = koinViewModel<SetupViewModel>()
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SetupEffect.NavigateToSession -> {
                onNavigateToSession(effect.sessionId)
            }

            is SetupEffect.ShowError -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    SetupScreenContent(
        state = state,
        onBackClick = onBackClick,
        onTopicSelected = { topic -> viewModel.onTopicSelected(topic) },
        onDifficultySelected = { difficulty -> viewModel.onDifficultySelected(difficulty) },
        onQuestionCountChanged = { count -> viewModel.onQuestionCountChanged(count) },
        onStartClicked = { viewModel.onStartClicked() }
    )
}

@Composable
fun SetupScreenContent(
    state: SetupState,
    onBackClick: () -> Unit,
    onTopicSelected: (Topic) -> Unit,
    onDifficultySelected: (Difficulty) -> Unit,
    onQuestionCountChanged: (Int) -> Unit,
    onStartClicked: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SetupTopBar(onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.PaddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge + Dimens.PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.setup_description),
                color = TextSecondary,
                fontSize = Dimens.TextSizeStandard,
                lineHeight = 20.sp
            )

            SectionHeader(title = stringResource(R.string.section_topic), icon = Icons.Default.Settings)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmallPlus),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmallPlus)
            ) {
                state.availableTopics.forEach { topic ->
                    TopicChip(
                        text = topic.displayName,
                        isSelected = state.selectedTopic == topic,
                        onClick = { onTopicSelected(topic) }
                    )
                }
            }

            SectionHeader(title = stringResource(R.string.section_difficulty), icon = null)
            DifficultySelector(
                selected = state.selectedDifficulty,
                onSelect = onDifficultySelected
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SectionHeader(title = stringResource(R.string.section_question_count), icon = null)
                Text(
                    text = stringResource(R.string.est_time_format, state.questionCount * 3),
                    color = TextSecondary,
                    fontSize = Dimens.TextSizeSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                listOf(1, 3, 5, 10).forEach { count ->
                    QuestionCountCard(
                        count = count,
                        isSelected = state.questionCount == count,
                        modifier = Modifier.weight(1f),
                        onClick = { onQuestionCountChanged(count) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            StartButton(
                isLoading = state.isLoading,
                onClick = onStartClicked
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SetupScreenPreview() {
    val mockState = SetupState(
        isLoading = false,
        availableTopics = Topic.entries,
        selectedTopic = Topic.ANDROID,
        selectedDifficulty = Difficulty.MIDDLE,
        questionCount = 5
    )

    SetupScreenContent(
        state = mockState,
        onBackClick = {},
        onTopicSelected = {},
        onDifficultySelected = {},
        onQuestionCountChanged = {},
        onStartClicked = {}
    )
}