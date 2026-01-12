package com.owl.ainterview.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.ProgressTrack
import com.owl.ainterview.ui.theme.SuccessGreenDark
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun MainStatsCard(averageScore: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(stringResource(R.string.avg_score_label), color = TextSecondary, fontSize = Dimens.TextSizeSmall)
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$averageScore",
                        color = TextPrimary,
                        fontSize = Dimens.TextSizeHuge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.out_of_100),
                        color = TextSecondary,
                        fontSize = Dimens.TextSizeLarge,
                        modifier = Modifier.padding(bottom = Dimens.PaddingSmall)
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
                Row(
                    modifier = Modifier
                        .background(SuccessGreenDark, RoundedCornerShape(Dimens.CornerRound))
                        .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Done, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(Dimens.SmallIcon))
                    Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
                    Text(stringResource(R.string.growth_this_week), color = NeonGreen, fontSize = Dimens.TextSizeMicro, fontWeight = FontWeight.Bold)
                }
            }

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { averageScore / 100f },
                    color = NeonGreen,
                    trackColor = ProgressTrack,
                    modifier = Modifier.size(Dimens.ProgressIndicatorSize),
                    strokeWidth = Dimens.ProgressStroke
                )
                Icon(Icons.Default.Star, contentDescription = null, tint = NeonGreen)
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.PaddingMedium)
    ) {
        Text(
            buildAnnotatedString {
                append(stringResource(R.string.home_greeting_prefix))
                withStyle(SpanStyle(color = NeonGreen, fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.home_greeting_name_default))
                }
            },
            fontSize = Dimens.TextSizeDisplay,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            lineHeight = Dimens.LineHeightDisplay
        )
    }
}