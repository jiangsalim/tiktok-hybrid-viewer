package com.tiktokviewer.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.theme.TikTokBlack
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit,
    onSavedVideosClick: () -> Unit,
    onWatchHistoryClick: () -> Unit,
    onPeerStatsClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Saved Videos", "Watch History", "Peer Stats")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokBlack)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Profile", color = TikTokWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onSettingsClick) {
                Text("⚙", fontSize = 22.sp)
            }
        }

        // Avatar + Name
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 36.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("TikTok Viewer User", color = TikTokWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("@local-user", color = Color.Gray, fontSize = 13.sp)
        }

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("12", "Watched today")
            StatItem("N/A", "Following", isComingSoon = true)
            StatItem("N/A", "Followers", isComingSoon = true)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Link account prompt
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Link your TikTok account", color = TikTokWhite, fontWeight = FontWeight.Bold)
                Text("See your followers, likes, and more.", color = Color.Gray, fontSize = 13.sp)
                TextButton(onClick = {}) {
                    Text("Notify me when ready", color = Color(0xFF1CE9CA))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            divider = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            title,
                            color = if (selectedTab == index) TikTokWhite else Color.Gray,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Tab content
        when (selectedTab) {
            0 -> SavedVideosGrid()
            1 -> WatchHistoryList()
            2 -> PeerStatsView()
        }
    }
}

@Composable
private fun StatItem(count: String, label: String, isComingSoon: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            count,
            color = if (isComingSoon) Color.Gray else TikTokWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun SavedVideosGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize().padding(4.dp),
        contentPadding = PaddingValues(2.dp)
    ) {
        items(9) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.DarkGray)
            )
        }
    }
}

@Composable
private fun WatchHistoryList() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Recent watches", color = TikTokWhite, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(8.dp))
        repeat(5) {
            Text(
                "Video ${it + 1} watched today",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun PeerStatsView() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🤝 Peer Sharing Contributions", color = TikTokWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        PeerStatCard("47", "Total searches helped")
        Spacer(modifier = Modifier.height(8.dp))
        PeerStatCard("12", "This week")
        Spacer(modifier = Modifier.height(8.dp))
        PeerStatCard("3.2s", "Avg response time")
        Spacer(modifier = Modifier.height(8.dp))
        PeerStatCard("0.92 ⭐", "Trust score")
    }
}

@Composable
private fun PeerStatCard(value: String, label: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, color = TikTokWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 13.sp)
        }
    }
}
