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
fun TimeWellbeingScreen(onBack: () -> Unit) {
    var dailyLimit by remember { mutableStateOf("Off") }
    var breakReminder by remember { mutableStateOf("Off") }

    Column(modifier = Modifier.fillMaxSize().background(TikTokBlack)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("←", color = TikTokWhite, fontSize = 20.sp) }
            Text("Time & Well-being", color = TikTokWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(40.dp))
        }

        Column(modifier = Modifier.background(Color(0xFF1A1A1A))) {
            // Screen time display
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Screen time today", color = Color.Gray, fontSize = 13.sp)
                Text("45 minutes", color = TikTokWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("This week: 3h 12m", color = Color.Gray, fontSize = 13.sp)
            }

            HorizontalDivider(color = Color.DarkGray)

            SettingsRow("Daily limit", subtitle = dailyLimit) {}
            SettingsRow("Break reminder", subtitle = breakReminder) {}
            SettingsRow("Family Pairing", subtitle = "Set up PIN") {}
        }
    }
}
