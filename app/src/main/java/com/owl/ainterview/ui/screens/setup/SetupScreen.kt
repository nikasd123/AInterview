package com.owl.ainterview.ui.screens.setup

import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.DarkBackground
import com.owl.domain.model.Difficulty
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetupScreen(
    onBackClick: () -> Unit,
    onNavigateToSession: (String) -> Unit
) {
    val viewModel = koinViewModel<SetupViewModel>()
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    // Обработка Side Effects (Навигация, Ошибки)
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

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            SetupTopBar(onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Header Description
            Text(
                text = "Customize your mock interview session. Select your stack, difficulty, and duration to get started.",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            // 1. Topic Section
            SectionHeader(title = "Select Topic", icon = Icons.Default.Settings) // Можно заменить иконку на < >
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.availableTopics.forEach { topic ->
                    TopicChip(
                        text = topic.displayName,
                        isSelected = state.selectedTopic == topic,
                        onClick = { viewModel.onTopicSelected(topic) }
                    )
                }
            }

            // 2. Difficulty Section
            SectionHeader(title = "Difficulty Level", icon = null) // Или иконка графика
            DifficultySelector(
                selected = state.selectedDifficulty,
                onSelect = { viewModel.onDifficultySelected(it) }
            )

            // 3. Question Count Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SectionHeader(title = "Number of Questions", icon = null)
                Text(
                    text = "EST: ${state.questionCount * 3} MIN",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(1 /*TODO 3*/, 5, 10).forEach { count ->
                    QuestionCountCard(
                        count = count,
                        isSelected = state.questionCount == count,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onQuestionCountChanged(count) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Start Button
            StartButton(
                isLoading = state.isLoading,
                onClick = { viewModel.onStartClicked() }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Компоненты ---

@Composable
fun SetupTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Interview Setup",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        } else {
            // Зеленая черточка, как в дизайне
            Box(modifier = Modifier.size(4.dp, 16.dp).background(NeonGreen, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TopicChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(if (isSelected) NeonGreen else DarkSurface, label = "bg")
    val contentColor by animateColorAsState(if (isSelected) Color.Black else Color.White, label = "content")

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun DifficultySelector(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Difficulty.entries.forEach { level ->
            val isSelected = level == selected
            val animatedBg by animateColorAsState(if (isSelected) NeonGreen else Color.Transparent, label = "diffBg")
            val animatedText by animateColorAsState(if (isSelected) Color.Black else TextSecondary, label = "diffText")

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(animatedBg)
                    .clickable { onSelect(level) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level.name.lowercase().replaceFirstChar { it.uppercase() }, // Junior, Middle...
                    color = animatedText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun QuestionCountCard(
    count: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(if (isSelected) NeonGreen else Color.Transparent, label = "border")

    Box(
        modifier = modifier
            .aspectRatio(1f) // Квадратная форма
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        // Галочка в углу
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = NeonGreen,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(16.dp)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when(count) {
                    3 -> "Short"
                    5 -> "Standard"
                    else -> "Extended"
                },
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun StartButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (isLoading) 0.95f else 1f, label = "scale")

    Button(
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonGreen,
            disabledContainerColor = NeonGreen.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.Black,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "START INTERVIEW",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}