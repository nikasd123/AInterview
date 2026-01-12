package com.owl.ainterview.ui.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    iconColor: Color,
    iconBgColor: Color = iconColor.copy(alpha = 0.1f),
    textColor: Color = TextPrimary,
    showChevron: Boolean = true,
    onClick: () -> Unit,
    endContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(Dimens.PaddingStandard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsIcon(icon, iconColor, iconBgColor)
        Spacer(modifier = Modifier.width(Dimens.PaddingStandard))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = textColor, fontSize = Dimens.TextSizeLarge)
            if (subtitle != null) {
                Text(subtitle, color = TextSecondary, fontSize = Dimens.TextSizeSmall, lineHeight = 16.sp)
            }
        }
        if (endContent != null) {
            endContent()
        } else if (showChevron) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
        }
    }
}