package com.tiktokviewer.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.theme.TikTokBlack
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun GracefulDegradationView(
    state: DegradationState,
    onRetry: () -> Unit,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = state.emoji,
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.title,
                color = TikTokWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.message,
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (state.canRetry) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1CE9CA)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Retry", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            if (state.canCancel && onCancel != null) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onCancel) {
                    Text("Cancel", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}

data class DegradationState(
    val emoji: String,
    val title: String,
    val message: String,
    val canRetry: Boolean = true,
    val canCancel: Boolean = true
)

object DegradationStates {
    fun noInternet() = DegradationState(
        emoji = "📡",
        title = "No internet connection",
        message = "Connect to the internet and try again.",
        canRetry = true
    )

    fun tiktokBlocked() = DegradationState(
        emoji = "🚫",
        title = "TikTok is being difficult",
        message = "Finding another path...",
        canRetry = true,
        canCancel = true
    )

    fun allAttemptsFailed() = DegradationState(
        emoji = "😔",
        title = "Couldn't fetch results",
        message = "Tap to retry or try again in a few minutes.",
        canRetry = true
    )

    fun noVideosFound() = DegradationState(
        emoji = "🔍",
        title = "No videos found",
        message = "Try a different search term.",
        canRetry = false,
        canCancel = true
    )

    fun backendDown() = DegradationState(
        emoji = "🛠️",
        title = "Service temporarily unavailable",
        message = "Our servers are waking up. This may take a moment.",
        canRetry = true
    )

    fun lowStorage() = DegradationState(
        emoji = "💾",
        title = "Storage almost full",
        message = "Some features are limited. Clear cache to free up space.",
        canRetry = true
    )
}
