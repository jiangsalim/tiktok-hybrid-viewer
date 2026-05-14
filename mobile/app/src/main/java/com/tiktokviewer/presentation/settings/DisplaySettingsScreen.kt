package com.tiktokviewer.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.theme.TikTokBlack
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun DisplaySettingsScreen(onBack: () -> Unit) {
    var darkMode by remember { mutableStateOf(true) }
    var quality by remember { mutableStateOf("Auto") }

    Column(modifier = Modifier.fillMaxSize().background(TikTokBlack)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("←", color = TikTokWhite, fontSize = 20.sp) }
            Text("Display", color = TikTokWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(40.dp))
        }

        Column(modifier = Modifier.background(Color(0xFF1A1A1A))) {
            ToggleRow("Dark mode", darkMode) { darkMode = it }
            SubtitleRow("Video quality", quality) { quality = it }
            SubtitleRow("Thumbnail grid", "Normal") {}
        }
    }
}

@Composable
private fun ToggleRow(title: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = TikTokWhite, fontSize = 15.sp)
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

@Composable
private fun SubtitleRow(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = TikTokWhite, fontSize = 15.sp)
        Text("$subtitle >", color = Color.Gray, fontSize = 14.sp)
    }
}
