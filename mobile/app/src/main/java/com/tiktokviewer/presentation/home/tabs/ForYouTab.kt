package com.tiktokviewer.presentation.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tiktokviewer.domain.model.VideoItem
import com.tiktokviewer.presentation.feed.VideoFeedScreen
import com.tiktokviewer.presentation.theme.TikTokBlack
import kotlinx.coroutines.launch

@Composable
fun ForYouTab(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var videos by remember { mutableStateOf<List<VideoItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // In production, this would call ForYouFeedGenerator
        isLoading = false
    }

    Box(modifier = modifier.fillMaxSize().background(TikTokBlack)) {
        if (isLoading) {
            // Skeleton loader
        } else if (videos.isNotEmpty()) {
            VideoFeedScreen(videos = videos)
        } else {
            // Empty state with trending fallback
        }
    }
}
