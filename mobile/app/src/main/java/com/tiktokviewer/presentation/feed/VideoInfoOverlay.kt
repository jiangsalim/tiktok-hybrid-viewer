package com.tiktokviewer.presentation.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.domain.model.VideoItem
import com.tiktokviewer.presentation.theme.TikTokWhite

@Composable
fun VideoInfoOverlay(
    video: VideoItem,
    onCreatorClick: () -> Unit,
    onHashtagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp, end = 80.dp)
    ) {
        // Creator name
        Row {
            Text(
                text = "@${video.author.username}",
                color = TikTokWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onCreatorClick() }
            )
            if (video.author.verified) {
                Text(
                    text = " ✅",
                    color = TikTokWhite,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Description
        Text(
            text = video.description,
            color = TikTokWhite,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Hashtags
        if (video.hashtags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                video.hashtags.take(3).forEach { tag ->
                    Text(
                        text = "#$tag",
                        color = TikTokWhite.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onHashtagClick(tag) }
                    )
                }
            }
        }

        // Music
        video.music?.let { music ->
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = "♫ ${music.songName}",
                    color = TikTokWhite.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}
