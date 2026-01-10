package com.owl.ainterview.ui.screens.session.compotents

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun ListeningControls(
    partialText: String,
    isMicActive: Boolean,
    onStopClick: () -> Unit,
    onCancelClick: () -> Unit,
    onStartClick: () -> Unit
) {
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
        remember { mutableFloatStateOf(1f) }
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
        remember { mutableFloatStateOf(0f) }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

        Box(contentAlignment = Alignment.Center) {
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isMicActive) NeonGreen else DarkSurface
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = if (isMicActive) Icons.Default.Check else Icons.Default.Call,
                    contentDescription = null,
                    tint = if (isMicActive) Color.Black else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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