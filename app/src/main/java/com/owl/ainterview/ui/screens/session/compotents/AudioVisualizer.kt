package com.owl.ainterview.ui.screens.session.compotents

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.owl.ainterview.ui.theme.NeonGreen
import java.nio.file.Files.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun AudioVisualizer(
    isAnimating: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 5,
    color: Color = NeonGreen
) {
    val infiniteTransition = rememberInfiniteTransition(label = "visualizer")

    // Создаем список анимированных значений высоты для каждой полоски
    val animations = List(barCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 300 + (index * 50), // Разная скорость для хаотичности
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar_$index"
        )
    }

    Canvas(modifier = modifier) {
        val barWidth = size.width / (barCount * 2) // Ширина полоски
        val maxBarHeight = size.height
        val gap = barWidth // Промежуток

        var startX = (size.width - (barCount * barWidth + (barCount - 1) * gap)) / 2

        animations.forEachIndexed { index, anim ->
            val currentHeight = if (isAnimating) maxBarHeight * anim.value else maxBarHeight * 0.2f
            val topY = (size.height - currentHeight) / 2 // Центрируем по вертикали

            drawRoundRect(
                color = color,
                topLeft = Offset(startX, topY),
                size = Size(barWidth, currentHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
            )
            startX += barWidth + gap
        }
    }
}