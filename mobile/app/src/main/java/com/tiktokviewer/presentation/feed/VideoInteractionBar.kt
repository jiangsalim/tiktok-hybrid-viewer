package com.tiktokviewer.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.domain.model.VideoItem
import com.tiktokviewer.presentation.theme.TikTokRed
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun VideoInteractionBar(
    video: VideoItem,
    isBookmarked: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(56.dp)
            .padding(end = 8.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Follow button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onFollowClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(TikTokRed),
                contentAlignment = Alignment.Center
            ) {
                Text("+", color = TikTokWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Like button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onLikeClick() }
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_compass),
                contentDescription = "Like",
                tint = TikTokWhite,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = formatCount(video.likeCount),
                color = TikTokWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // Comment button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onCommentClick() }
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_compass),
                contentDescription = "Comments",
                tint = TikTokWhite,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = formatCount(video.commentCount.toLong()),
                color = TikTokWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // Bookmark button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onBookmarkClick() }
        ) {
            Icon(
                painter = painterResource(id = if (isBookmarked) android.R.drawable.ic_menu_compass else android.R.drawable.ic_menu_compass),
                contentDescription = "Bookmark",
                tint = if (isBookmarked) Color(0xFFFFCC00) else TikTokWhite,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = formatCount(video.shareCount.toLong()),
                color = TikTokWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // Share button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onShareClick() }
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_compass),
                contentDescription = "Share",
                tint = TikTokWhite,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = formatCount(video.shareCount.toLong()),
                color = TikTokWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000f)
        count >= 1_000 -> String.format("%.1fK", count / 1_000f)
        else -> count.toString()
    }
}
