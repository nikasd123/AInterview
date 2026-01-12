package com.owl.ainterview.ui.screens.setup.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen

@Composable
fun StartButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (isLoading) 0.95f else 1f, label = "scale")

    Button(
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonGreen,
            disabledContainerColor = NeonGreen.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightStandard)
            .scale(scale),
        shape = RoundedCornerShape(Dimens.ButtonCornerRadius)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimens.BadgeSize),
                color = Color.Black,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = stringResource(R.string.btn_start_interview),
                color = Color.Black,
                fontSize = Dimens.TextSizeLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.cd_start_arrow),
                tint = Color.Black
            )
        }
    }
}