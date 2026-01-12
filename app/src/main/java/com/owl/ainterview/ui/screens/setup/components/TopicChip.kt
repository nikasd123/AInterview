package com.owl.ainterview.ui.screens.setup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextPrimary

@Composable
fun TopicChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(if (isSelected) NeonGreen else MaterialTheme.colorScheme.surface, label = "bg")
    val contentColor by animateColorAsState(if (isSelected) Color.Black else TextPrimary, label = "content")

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.ChipCornerRadius))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.ChipPaddingH, vertical = Dimens.ChipPaddingV)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = Dimens.TextSizeStandard
        )
    }
}