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
fun StorageSettingsScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(TikTokBlack)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("←", color = TikTokWhite, fontSize = 20.sp) }
            Text("Free up space", color = TikTokWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(40.dp))
        }

        Column(modifier = Modifier.background(Color(0xFF1A1A1A)).padding(16.dp)) {
            StorageRow("Video cache", "42 MB", true)
            StorageRow("Thumbnail cache", "18 MB", true)
            StorageRow("Search cache", "2 MB", true)
            StorageRow("Interest data", "1 MB", true)
            StorageRow("Saved videos", "15 MB", false)

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1CE9CA))
            ) {
                Text("Clear All Cache", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StorageRow(label: String, size: String, canClear: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, color = TikTokWhite, fontSize = 15.sp)
            Text(size, color = Color.Gray, fontSize = 13.sp)
        }
        if (canClear) {
            TextButton(onClick = {}) {
                Text("Clear", color = Color(0xFF1CE9CA), fontSize = 14.sp)
            }
        }
    }
    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
}
