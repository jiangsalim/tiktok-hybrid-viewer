package com.tiktokviewer.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.theme.TikTokBlack
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onDisplayClick: () -> Unit,
    onPlaybackClick: () -> Unit,
    onContentPreferencesClick: () -> Unit,
    onPeerSettingsClick: () -> Unit,
    onStorageClick: () -> Unit,
    onTimeWellbeingClick: () -> Unit,
    onHelpCenterClick: () -> Unit,
    onPrivacyCenterClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokBlack)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onNavigateBack) {
                Text("← Back", color = TikTokWhite, fontSize = 16.sp)
            }
            Text("Settings", color = TikTokWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(60.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingsSection("APPEARANCE & PLAYBACK") {
                SettingsRow("Display", onClick = onDisplayClick)
                SettingsRow("Playback", onClick = onPlaybackClick)
                SettingsRow("Language", subtitle = "English")
                SettingsRow("Accessibility")
            }

            SettingsSection("CONTENT & INTERESTS") {
                SettingsRow("Content preferences", onClick = onContentPreferencesClick)
                SettingsRow("Activity center")
                SettingsRow("Notifications")
            }

            SettingsSection("PEER SHARING") {
                SettingsRow("Peer Sharing", subtitle = "ON", onClick = onPeerSettingsClick)
                SettingsRow("Your peer stats")
            }

            SettingsSection("STORAGE & DATA") {
                SettingsRow("Offline videos")
                SettingsRow("Free up space", subtitle = "62 MB", onClick = onStorageClick)
                SettingsRow("Data Saver", subtitle = "ON")
            }

            SettingsSection("TIME & WELL-BEING") {
                SettingsRow("Screen time", subtitle = "45m today", onClick = onTimeWellbeingClick)
                SettingsRow("Daily limit", subtitle = "Off")
                SettingsRow("Family Pairing")
            }

            SettingsSection("TIKTOK ACCOUNT (COMING SOON)") {
                SettingsRow("Link TikTok account", subtitle = "🔜")
                SettingsRow("Following list", subtitle = "🔜")
                SettingsRow("Liked videos", subtitle = "🔜")
            }

            SettingsSection("SUPPORT & ABOUT") {
                SettingsRow("Help Center", onClick = onHelpCenterClick)
                SettingsRow("Privacy Center", onClick = onPrivacyCenterClick)
                SettingsRow("Terms and Policies")
                SettingsRow("App version", subtitle = "1.0.0")
                SettingsRow("Share this app")
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Text(
        title,
        color = Color.Gray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
    )
    Column(modifier = Modifier.background(Color(0xFF1A1A1A))) {
        content()
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = TikTokWhite, fontSize = 15.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (subtitle != null) {
                Text(subtitle, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
            }
            if (onClick != null) {
                Text(">", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
}
