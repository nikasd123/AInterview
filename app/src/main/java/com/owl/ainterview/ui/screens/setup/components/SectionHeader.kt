package com.owl.ainterview.ui.screens.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextPrimary

@Composable
fun SectionHeader(title: String, icon: ImageVector?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonGreen,
                modifier = Modifier.size(Dimens.SectionIconSize)
            )
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
        } else {
            Box(modifier = Modifier.size(Dimens.SectionIndicatorWidth, Dimens.SectionIndicatorHeight).background(NeonGreen, CircleShape))
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
        }
        Text(
            text = title,
            color = TextPrimary,
            fontSize = Dimens.TextSizeTitle,
            fontWeight = FontWeight.Bold
        )
    }
}