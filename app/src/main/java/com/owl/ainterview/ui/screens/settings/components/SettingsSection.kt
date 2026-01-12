package com.owl.ainterview.ui.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            color = TextSecondary,
            fontSize = Dimens.TextSizeSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = Dimens.PaddingMedium, bottom = Dimens.PaddingMedium)
        )
        content()
    }
}