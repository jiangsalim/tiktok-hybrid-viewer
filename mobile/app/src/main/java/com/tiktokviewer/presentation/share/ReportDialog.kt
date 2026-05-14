package com.tiktokviewer.presentation.share

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun ReportDialog(
    videoId: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedReason by remember { mutableStateOf<String?>(null) }

    val reasons = listOf(
        "It's spam",
        "Inappropriate content",
        "Violent or graphic",
        "Hate speech",
        "Harassment",
        "Misinformation",
        "Other"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp))
                .clickable(enabled = false) {}
                .padding(20.dp)
        ) {
            Text(
                "Report video",
                color = TikTokWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                "Why are you reporting this video?",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            reasons.forEach { reason ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedReason = reason }
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedReason == reason,
                        onClick = { selectedReason = reason },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF1CE9CA)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(reason, color = TikTokWhite, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val url = "https://www.tiktok.com/legal/report/video?video_id=$videoId"
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                    context.startActivity(intent)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedReason != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1CE9CA)
                )
            ) {
                Text("Submit report", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Text(
                "Report goes to TikTok. This app does not review or moderate content.",
                color = Color.Gray,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
