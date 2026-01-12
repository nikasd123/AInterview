package com.owl.ainterview.ui.screens.session.compotents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextPrimary

@Composable
fun SessionProgressBar(current: Int, total: Int) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.PaddingLarge)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.session_question_progress, current, total),
                color = TextPrimary,
                fontSize = Dimens.TextSizeSmall
            )
            Text(
                text = stringResource(R.string.session_percent_completed, (current.toFloat() / total * 100).toInt()),
                color = NeonGreen,
                fontSize = Dimens.TextSizeSmall
            )
        }
        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        LinearProgressIndicator(
            progress = { current.toFloat() / total.coerceAtLeast(1) },
            modifier = Modifier.fillMaxWidth().height(Dimens.ProgressBarHeight).clip(RoundedCornerShape(2.dp)),
            color = NeonGreen,
            trackColor = MaterialTheme.colorScheme.surface
        )
    }
}