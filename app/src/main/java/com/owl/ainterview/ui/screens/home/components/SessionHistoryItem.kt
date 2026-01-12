package com.owl.ainterview.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.owl.ainterview.ui.theme.ErrorRed
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.NeonOrange
import com.owl.ainterview.ui.theme.NeonRed
import com.owl.ainterview.ui.theme.StatIconRed
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.InterviewSession
import java.time.format.DateTimeFormatter

@Composable
fun SessionHistoryItem(session: InterviewSession, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")

    val scoreColor = when {
        session.averageScore >= 80 -> NeonGreen
        session.averageScore >= 50 -> NeonOrange
        else -> ErrorRed
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(Dimens.CornerRound.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium).padding(start = Dimens.PaddingMedium, end = Dimens.PaddingStandard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.SessionIconBoxSize)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Build, contentDescription = null, tint = StatIconRed)
            }

            Spacer(modifier = Modifier.width(Dimens.SpacerLarge))

            Column(modifier = Modifier.weight(1f)) {
                Text(session.settings.topic.displayName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = Dimens.TextSizeStandard)
                Text(session.date.format(formatter), color = TextSecondary, fontSize = Dimens.TextSizeSmall)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${session.averageScore}/100",
                    color = scoreColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimens.TextSizeStandard
                )
                Text(
                    text =
                        if(session.averageScore >= 60) stringResource(R.string.status_passed)
                        else stringResource(R.string.status_needs_work),
                    color = TextSecondary,
                    fontSize = Dimens.TextSizeMicro
                )
            }
        }
    }
}