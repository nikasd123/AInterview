package com.owl.ainterview.ui.screens.report.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurfaceVariant
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.FeedbackBackground
import com.owl.ainterview.ui.theme.FeedbackBorder
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.NeonOrange
import com.owl.ainterview.ui.theme.NeonRed
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.Question

// --- 1. Score Circle ---
@Composable
fun ScoreCircle(
    score: Int,
    modifier: Modifier = Modifier,
    size: Dp = Dimens.ScoreCircleSize,
    strokeWidth: Dp = Dimens.ScoreCircleStroke,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = score / 100f,
        animationSpec = tween(durationMillis = Dimens.AnimDurationLong),
        label = "score"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background Circle (Grey)
            drawArc(
                color = DarkSurfaceVariant,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // Foreground Circle (Green)
            drawArc(
                color = NeonGreen,
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = score.toString(),
                color = TextPrimary,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.total_score),
                color = TextSecondary,
                fontSize = Dimens.TextSizeStandard
            )
        }
    }
}

// --- 2. Feedback Summary Card ---
@Composable
fun FeedbackSummaryCard(summary: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = NeonGreen)
                Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
                Text(
                    text = stringResource(R.string.ai_feedback_title),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimens.TextSizeTitle
                )
            }
            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
            Text(
                text = summary.ifBlank { stringResource(R.string.feedback_default) },
                color = TextSecondary,
                fontSize = Dimens.TextSizeStandard,
                lineHeight = 20.sp
            )
        }
    }
}

// --- 3. Expandable Question Item ---
@Composable
fun QuestionResultItem(
    index: Int,
    question: Question,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
) {
    val score = question.rating ?: 0
    val scoreColor = when {
        score >= 8 -> NeonGreen
        score >= 5 -> NeonOrange
        else -> NeonRed
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(Dimens.ButtonCornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onExpandClick)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingStandard)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    // Number Badge
                    Box(
                        modifier = Modifier
                            .size(Dimens.BadgeSize)
                            .background(scoreColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            index.toString(),
                            color = scoreColor,
                            fontSize = Dimens.TextSizeSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.SpacerMedium))

                    // Question Text
                    Column {
                        Text(
                            text = question.text,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = Dimens.TextSizeStandard,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 2
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Tag
                        Text(
                            text = question.topic.displayName.uppercase(),
                            color = TextSecondary,
                            fontSize = Dimens.TextSizeMicro,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.Black, RoundedCornerShape(Dimens.PaddingSmall))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Score & Chevron
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.question_score_format, score),
                        color = scoreColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.TextSizeStandard
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.cd_expand_collapse),
                        tint = TextSecondary
                    )
                }
            }

            // Expanded Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = Dimens.PaddingStandard)) {
                    // Divider
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        thickness = Dimens.BorderThin
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

                    // 1. User Answer
                    Text(
                        text = stringResource(R.string.label_your_answer),
                        color = TextSecondary,
                        fontSize = Dimens.TextSizeMicro,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                Dimens.BorderThin,
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(Dimens.PaddingMedium)
                            )
                            .padding(Dimens.SpacerMedium)
                    ) {
                        Text(
                            text = question.userAnswerText
                                ?: stringResource(R.string.label_no_answer),
                            color = TextPrimary,
                            fontSize = Dimens.TextSizeStandard
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

                    // 2. AI Feedback / Ideal Answer
                    Text(
                        text = stringResource(R.string.label_ai_feedback),
                        color = NeonGreen,
                        fontSize = Dimens.TextSizeMicro,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                FeedbackBackground,
                                RoundedCornerShape(Dimens.PaddingMedium)
                            )
                            .border(
                                Dimens.BorderThin,
                                FeedbackBorder,
                                RoundedCornerShape(Dimens.PaddingMedium)
                            )
                            .padding(Dimens.SpacerMedium)
                    ) {
                        Text(
                            text = question.aiFeedback
                                ?: stringResource(R.string.label_no_feedback),
                            color = TextPrimary,
                            fontSize = Dimens.TextSizeStandard
                        )
                    }
                }
            }
        }
    }
}