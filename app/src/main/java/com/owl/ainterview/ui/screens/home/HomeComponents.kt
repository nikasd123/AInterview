package com.owl.ainterview.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.theme.DarkSurface
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.InterviewSession
import java.time.format.DateTimeFormatter

@Composable
fun MainStatsCard(averageScore: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Average Score", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$averageScore",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/100",
                        color = TextSecondary,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Trend badge
                Row(
                    modifier = Modifier
                        .background(Color(0xFF1B5E20), RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Done, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+5% THIS WEEK", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Circular Indicator (Mini)
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { averageScore / 100f },
                    color = NeonGreen,
                    trackColor = Color.DarkGray,
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 6.dp
                )
                Icon(Icons.Default.Star, contentDescription = null, tint = NeonGreen)
            }
        }
    }
}

@Composable
fun SmallStatCard(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(title, color = TextSecondary, fontSize = 12.sp)
            Text(subtext, color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
fun SessionHistoryItem(session: InterviewSession, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")

    // Цвет оценки
    val scoreColor = when {
        session.averageScore >= 80 -> NeonGreen
        session.averageScore >= 50 -> Color(0xFFFFC107)
        else -> Color(0xFFCF6679)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(50.dp), // Сильное закругление как на макете
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp).padding(start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF2C2C2E), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFFE57373))
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Text(session.settings.topic.displayName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(session.date.format(formatter), color = TextSecondary, fontSize = 12.sp)
            }

            // Score Badge
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${session.averageScore}/100",
                    color = scoreColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = if(session.averageScore >= 60) "Passed" else "Needs Work",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
        }
    }
}