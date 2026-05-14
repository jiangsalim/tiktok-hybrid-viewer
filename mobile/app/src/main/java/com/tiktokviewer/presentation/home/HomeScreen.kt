package com.tiktokviewer.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.home.tabs.ExploreTab
import com.tiktokviewer.presentation.home.tabs.ForYouTab
import com.tiktokviewer.presentation.theme.TikTokBlack
import com.tiktokviewer.presentation.theme.TikTokTurquoise
import com.tiktokviewer.presentation.theme.TikTokWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("LIVE", "Explore", "Following", "For You")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(3) }

    Column(modifier = modifier.fillMaxSize().background(TikTokBlack)) {
        // Top tab row + search icon
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.weight(1f),
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 2.dp,
                        color = TikTokWhite
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == index) TikTokWhite else Color.Gray,
                                fontSize = if (selectedTab == index) 16.sp else 15.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Search icon
            TextButton(onClick = onSearchClick) {
                Text("🔍", fontSize = 20.sp)
            }
        }

        // Pager content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ComingSoonPlaceholder("LIVE")
                1 -> ExploreTab()
                2 -> ComingSoonPlaceholder("Following")
                3 -> ForYouTab()
            }
        }
    }
}

@Composable
fun ComingSoonPlaceholder(feature: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(TikTokBlack),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Text("🔜", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "$feature - Coming Soon",
                color = TikTokWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "This feature requires TikTok account integration.\nOur team is working on it.",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {}) {
                Text("Notify me when ready")
            }
        }
    }
}
