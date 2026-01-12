package com.owl.ainterview.ui.screens.session.compotents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.RecordingDot
import com.owl.ainterview.ui.theme.TextPrimary

@Composable
fun SessionTopBar(
    topicTitle: String,
    timer: String,
    onBackClick: () -> Unit,
    onEndClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingStandard),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back), tint = TextPrimary)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = topicTitle.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(Dimens.TimerDotSize)
                        .background(RecordingDot, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = timer,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        TextButton(onClick = onEndClick) {
            Text(stringResource(R.string.session_end), color = NeonGreen)
        }
    }
}