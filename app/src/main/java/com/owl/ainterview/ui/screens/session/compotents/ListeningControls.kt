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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextPrimary
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
            text = if (isMicActive) stringResource(R.string.mic_state_listening) else stringResource(R.string.mic_state_paused),
            color = if (isMicActive) NeonGreen else TextSecondary,
            fontSize = Dimens.TextSizeSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        Text(
            text = if (isMicActive) stringResource(R.string.mic_hint_active) else stringResource(R.string.mic_hint_paused),
            color = TextPrimary,
            fontSize = Dimens.TextSizeDisplay,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

        Text(
            text = when {
                !isMicActive && partialText.isBlank() -> stringResource(R.string.speech_placeholder_stopped)
                partialText.isBlank() -> stringResource(R.string.speech_placeholder_listening)
                else -> "\"$partialText\""
            },
            color = TextSecondary,
            fontSize = Dimens.TextSizeLarge,
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
                        .size(Dimens.MicPulseSize)
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
                    .size(Dimens.MicButtonSize)
                    .shadow(elevation = Dimens.PaddingMedium, shape = CircleShape, spotColor = NeonGreen),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isMicActive) NeonGreen else MaterialTheme.colorScheme.surface
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = if (isMicActive) Icons.Default.Check else Icons.Default.MicOff,
                    contentDescription = null,
                    tint = if (isMicActive) Color.Black else TextPrimary,
                    modifier = Modifier.size(Dimens.MicIconSize)
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onCancelClick,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cd_cancel), tint = TextSecondary)
            }

            Spacer(modifier = Modifier.width(Dimens.SpacerLarge))

            Button(
                onClick = onStopClick,
                colors = ButtonDefaults.buttonColors(containerColor = TextPrimary),
                shape = RoundedCornerShape(Dimens.CornerRound)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
                Text(stringResource(R.string.btn_stop_submit), color = Color.Black)
            }
        }
    }
}