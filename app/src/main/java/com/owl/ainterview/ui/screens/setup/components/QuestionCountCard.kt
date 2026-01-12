package com.owl.ainterview.ui.screens.setup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun QuestionCountCard(
    count: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(if (isSelected) NeonGreen else Color.Transparent, label = "border")

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(Dimens.QuestionCardCorner))
            .background(MaterialTheme.colorScheme.surface)
            .border(Dimens.QuestionCardBorder, borderColor, RoundedCornerShape(Dimens.QuestionCardCorner))
            .clickable(onClick = onClick)
            .padding(Dimens.QuestionCardPadding)
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = NeonGreen,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(Dimens.QuestionCardCheckSize)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                color = TextPrimary,
                fontSize = Dimens.TextSizeDisplay, // 24.sp
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
            Text(
                text = stringResource(
                    when(count) {
                        1 -> R.string.duration_quick
                        3 -> R.string.duration_short
                        5 -> R.string.duration_standard
                        else -> R.string.duration_extended
                    }
                ),
                color = TextSecondary,
                fontSize = Dimens.TextSizeSmall
            )
        }
    }
}