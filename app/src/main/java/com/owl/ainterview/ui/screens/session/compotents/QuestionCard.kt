package com.owl.ainterview.ui.screens.session.compotents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.SurfaceTransparent
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun QuestionCard(text: String, isSpeaking: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingLarge),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingLarge),
            horizontalAlignment = Alignment.Start
        ) {
            // Badge
            if (isSpeaking) {
                Row(
                    modifier = Modifier
                        .background(SurfaceTransparent, RoundedCornerShape(Dimens.HintBackgroundCorner))
                        .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AudioVisualizer(isAnimating = true, modifier = Modifier.size(Dimens.VisualizerBarWidth, Dimens.VisualizerBarHeight))
                    Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
                    Text(stringResource(R.string.session_ai_speaking), color = TextSecondary, fontSize = Dimens.TextSizeMicro)
                }
                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))
            }

            // Text
            Text(
                text = text,
                color = TextPrimary,
                fontSize = Dimens.TextSizeHeader,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))

            Text(
                text = stringResource(R.string.session_default_hint),
                color = TextSecondary,
                fontSize = Dimens.TextSizeStandard
            )
        }
    }
}