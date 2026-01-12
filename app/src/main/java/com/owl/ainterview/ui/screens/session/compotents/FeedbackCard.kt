package com.owl.ainterview.ui.screens.session.compotents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.SurfaceTransparent
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary

@Composable
fun FeedbackCard(score: Int, feedback: String, onNextClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(Dimens.ButtonCornerRadius),
        border = BorderStroke(Dimens.FeedbackBorder, NeonGreen.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingStandard)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(R.string.feedback_strong_answer), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = Dimens.TextSizeTitle)
                    Text(stringResource(R.string.feedback_concepts_covered), color = TextSecondary, fontSize = Dimens.TextSizeSmall)
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(4.dp, NeonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$score%", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = Dimens.TextSizeSmall)
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            Text(
                text = feedback,
                color = TextSecondary,
                fontSize = Dimens.TextSizeStandard,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background(SurfaceTransparent, RoundedCornerShape(Dimens.HintBackgroundCorner))
                    .padding(Dimens.SpacerMedium)
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text(stringResource(R.string.btn_next_question), color = Color.Black)
            }
        }
    }
}