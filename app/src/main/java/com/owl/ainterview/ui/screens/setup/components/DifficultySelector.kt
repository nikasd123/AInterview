package com.owl.ainterview.ui.screens.setup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.Difficulty

@Composable
fun DifficultySelector(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.SelectorCornerRadius))
            .background(MaterialTheme.colorScheme.surface)
            .padding(Dimens.SelectorPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Difficulty.entries.forEach { level ->
            val isSelected = level == selected
            val animatedBg by animateColorAsState(if (isSelected) NeonGreen else Color.Transparent, label = "diffBg")
            val animatedText by animateColorAsState(if (isSelected) Color.Black else TextSecondary, label = "diffText")

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(Dimens.SelectorInnerCornerRadius))
                    .background(animatedBg)
                    .clickable { onSelect(level) }
                    .padding(vertical = Dimens.SelectorItemPaddingV),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = animatedText,
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimens.TextSizeStandard
                )
            }
        }
    }
}