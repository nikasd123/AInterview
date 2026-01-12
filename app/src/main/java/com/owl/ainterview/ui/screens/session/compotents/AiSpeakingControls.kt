package com.owl.ainterview.ui.screens.session.compotents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun AiSpeakingControls(
    onSkip: () -> Unit,
    onRepeat: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.PaddingLarge),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onRepeat) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(Dimens.PaddingStandard), tint = TextSecondary)
            Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
            Text(stringResource(R.string.btn_repeat), color = TextSecondary)
        }

        Button(
            onClick = onSkip,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(Dimens.CornerRound)
        ) {
            Text(stringResource(R.string.btn_skip_reading), color = NeonGreen)
        }
    }
}