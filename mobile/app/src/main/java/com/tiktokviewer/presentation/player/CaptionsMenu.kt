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
fun CaptionsMenu(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    captionsEnabled: Boolean,
    onCaptionsToggle: () -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    if (!isVisible) return

    val languages = listOf(
        "English", "Swahili", "French", "Spanish",
        "Arabic", "Hindi", "Portuguese", "German",
        "Chinese", "Japanese", "Korean"
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
                "Captions & Translation",
                color = TikTokWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Captions", color = TikTokWhite, fontSize = 16.sp)
                Switch(
                    checked = captionsEnabled,
                    onCheckedChange = { onCaptionsToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF1CE9CA),
                        checkedTrackColor = Color(0xFF1CE9CA).copy(alpha = 0.4f)
                    )
                )
            }

            HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

            Text(
                "Translate captions to",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            languages.take(6).forEach { lang ->
                val isSelected = lang == selectedLanguage
                Text(
                    lang,
                    color = if (isSelected) Color(0xFF1CE9CA) else TikTokWhite,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageChange(lang) }
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}
