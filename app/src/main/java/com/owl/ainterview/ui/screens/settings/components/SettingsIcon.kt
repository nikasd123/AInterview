package com.owl.ainterview.ui.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.owl.ainterview.ui.theme.Dimens

@Composable
fun SettingsIcon(
    icon: ImageVector,
    color: Color,
    bgColor: Color = color.copy(alpha = 0.1f)
) {
    Box(
        modifier = Modifier
            .size(Dimens.SettingsIconBoxSize)
            .background(bgColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(Dimens.SettingsIconSize))
    }
}