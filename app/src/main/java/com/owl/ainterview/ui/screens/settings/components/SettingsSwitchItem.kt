package com.owl.ainterview.ui.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.DarkBackground
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingStandard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsIcon(icon, if (isChecked) NeonGreen else TextSecondary)
        Spacer(modifier = Modifier.width(Dimens.PaddingStandard))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = TextPrimary, fontSize = Dimens.TextSizeLarge)
            Text(subtitle, color = TextSecondary, fontSize = Dimens.TextSizeSmall)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NeonGreen,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.background
            )
        )
    }
}