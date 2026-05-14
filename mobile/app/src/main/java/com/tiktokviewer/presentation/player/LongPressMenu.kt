package com.tiktokviewer.presentation.player

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun LongPressMenu(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    autoScrollEnabled: Boolean,
    onAutoScrollToggle: () -> Unit,
    captionsEnabled: Boolean,
    onCaptionsToggle: () -> Unit,
    pipEnabled: Boolean,
    onPipToggle: () -> Unit,
    backgroundAudioEnabled: Boolean,
    onBackgroundAudioToggle: () -> Unit,
    clearDisplayEnabled: Boolean,
    onClearDisplayToggle: () -> Unit,
    onDownload: () -> Unit,
    onNotInterested: () -> Unit,
    onReport: () -> Unit,
    onWhyThisPost: () -> Unit
) {
    if (!isVisible) return

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
                .padding(16.dp)
        ) {
            // Top actions
            LongPressActionItem("Download") { onDownload(); onDismiss() }
            LongPressActionItem("Not interested") { onNotInterested(); onDismiss() }
            LongPressActionItem("Report") { onReport(); onDismiss() }

            HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))

            // Speed selector
            Text("Speed", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { speed ->
                    val isSelected = currentSpeed == speed
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF1CE9CA) else Color.DarkGray)
                            .clickable { onSpeedChange(speed) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "${speed}x",
                            color = if (isSelected) Color.Black else TikTokWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))

            // Toggles
            LongPressToggleItem("Clear display", clearDisplayEnabled, onClearDisplayToggle)
            LongPressToggleItem("Auto scroll", autoScrollEnabled, onAutoScrollToggle)
            LongPressToggleItem("Aa Captions & translation", captionsEnabled, onCaptionsToggle)
            LongPressToggleItem("Picture-in-Picture", pipEnabled, onPipToggle)
            LongPressToggleItem("Background audio", backgroundAudioEnabled, onBackgroundAudioToggle)

            HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))

            // Why this post
            LongPressActionItem("Why this post") { onWhyThisPost(); onDismiss() }
        }
    }
}

@Composable
private fun LongPressActionItem(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        color = TikTokWhite,
        fontSize = 15.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    )
}

@Composable
private fun LongPressToggleItem(label: String, isEnabled: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TikTokWhite, fontSize = 15.sp)
        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF1CE9CA),
                checkedTrackColor = Color(0xFF1CE9CA).copy(alpha = 0.4f)
            )
        )
    }
}
