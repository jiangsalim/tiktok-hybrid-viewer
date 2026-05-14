package com.tiktokviewer.presentation.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiktokviewer.presentation.theme.TikTokWhite

data class CommentItem(
    val username: String,
    val text: String,
    val likes: Int,
    val timeAgo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(
    comments: List<CommentItem>,
    commentCount: Int,
    onDismiss: () -> Unit,
    onCommentOnTikTok: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Comments",
                    color = TikTokWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$commentCount",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Divider(color = Color.DarkGray)

            // Comment list
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(comments) { comment ->
                    CommentRow(comment)
                }
            }

            // Bottom prompt
            Divider(color = Color.DarkGray)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCommentOnTikTok() }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Comment on TikTok →",
                    color = Color(0xFF1CE9CA),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CommentRow(comment: CommentItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.DarkGray, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                comment.username,
                color = TikTokWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                comment.text,
                color = TikTokWhite.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(comment.timeAgo, color = Color.Gray, fontSize = 11.sp)
                Text("Reply", color = Color.Gray, fontSize = 11.sp)
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("❤", fontSize = 16.sp)
            Text("${comment.likes}", color = Color.Gray, fontSize = 11.sp)
        }
    }
}
