package com.tiktokviewer.presentation.share

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.domain.model.VideoItem
import com.tiktokviewer.presentation.theme.TikTokBlack
import com.tiktokviewer.presentation.theme.TikTokWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareSheet(
    video: VideoItem,
    onDismiss: () -> Unit,
    onDownload: () -> Unit,
    onNotInterested: () -> Unit,
    onReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Video caption
            Text(
                text = video.description.ifEmpty { "Check out this video" },
                color = TikTokWhite,
                fontSize = 14.sp,
                maxLines = 2,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Contact quick share row (Coming Soon)
            Text(
                text = "Quick share coming soon with account integration",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Divider(color = Color.DarkGray)

            // Share actions grid
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ShareActionItem("Copy Link", "🔗") {
                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip = android.content.ClipData.newPlainText("TikTok Link", "https://vm.tiktok.com/${video.videoId}")
                    clipboard.setPrimaryClip(clip)
                    onDismiss()
                }
                ShareActionItem("WhatsApp", "💬") {
                    shareToApp(context, "com.whatsapp", video)
                    onDismiss()
                }
                ShareActionItem("SMS", "📱") {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out this TikTok: https://vm.tiktok.com/${video.videoId}")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via SMS"))
                    onDismiss()
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ShareActionItem("Email", "📧") {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_SUBJECT, "Check out this video")
                        putExtra(Intent.EXTRA_TEXT, "https://vm.tiktok.com/${video.videoId}")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via Email"))
                    onDismiss()
                }
                ShareActionItem("Facebook", "📘") {
                    shareToApp(context, "com.facebook.katana", video)
                    onDismiss()
                }
                ShareActionItem("More", "⋯") {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out this TikTok: https://vm.tiktok.com/${video.videoId}")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share"))
                    onDismiss()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            // Bottom actions
            ShareBottomAction("Download", onDownload)
            ShareBottomAction("Not interested", onNotInterested)
            ShareBottomAction("Report", onReport)

            // Coming Soon items
            ShareBottomAction("Add to Story (Coming Soon)", {}, enabled = false)
            ShareBottomAction("Duet (Coming Soon)", {}, enabled = false)
            ShareBottomAction("Story (Coming Soon)", {}, enabled = false)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ShareActionItem(label: String, emoji: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = TikTokWhite,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ShareBottomAction(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    val color = if (enabled) TikTokWhite else Color.Gray
    Text(
        text = label,
        color = color,
        fontSize = 15.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 14.dp)
    )
}

private fun shareToApp(context: android.content.Context, packageName: String, video: VideoItem) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            `package` = packageName
            putExtra(Intent.EXTRA_TEXT, "Check out this TikTok: https://vm.tiktok.com/${video.videoId}")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "https://vm.tiktok.com/${video.videoId}")
        }
        context.startActivity(Intent.createChooser(intent, "Share"))
    }
}
