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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.screens.report.components.FeedbackSummaryCard
import com.owl.ainterview.ui.screens.report.components.QuestionResultItem
import com.owl.ainterview.ui.screens.report.components.ScoreCircle
import com.owl.ainterview.ui.theme.DarkBackground
import com.owl.ainterview.ui.theme.NeonGreen
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ReportScreen(
    onHomeClick: () -> Unit
) {
    val viewModel = koinViewModel<ReportViewModel>()
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when(effect) {
            ReportEffect.NavigateHome -> onHomeClick()
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            ReportTopBar(onBackClick = { viewModel.onFinishClick() }) // Кнопка назад тоже ведет домой
        },
        bottomBar = {
            // Кнопка Finish Review внизу
            Button(
                onClick = { viewModel.onFinishClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Finish Review", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        if (state.isLoading || state.session == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            val session = state.session!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 1. Score Circle Area
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Great Job!", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(24.dp))
                        ScoreCircle(score = session.averageScore)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Badge "Top 10%"
                        if (session.averageScore > 80) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF1B5E20), RoundedCornerShape(50))
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text("Top 10% of candidates", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 2. Feedback Summary
                item {
                    // Пока берем фидбек из первого вопроса или общий заглушечный,
                    // т.к. в Session мы не генерировали общий фидбек на всё интервью (можно добавить в будущем)
                    FeedbackSummaryCard(summary = "You demonstrated strong skills in ${session.settings.topic.displayName}. Keep practicing consistent hashing and edge cases.")
                }

                // 3. Detailed Analysis Header
                item {
                    Text("Detailed Analysis", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // 4. Questions List
                itemsIndexed(session.questions) { index, question ->
                    QuestionResultItem(
                        index = index + 1,
                        question = question,
                        isExpanded = state.expandedQuestionIds.contains(question.id),
                        onExpandClick = { viewModel.toggleQuestionExpansion(question.id) }
                    )
                }

                // Extra spacer for BottomBar
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun ReportTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        Text("Interview Report", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        IconButton(onClick = { /* Share Logic */ }) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
        }
    }
}