package com.tiktokviewer.domain.model

data class VideoItem(
    val videoId: String,
    val description: String,
    val author: AuthorInfo,
    val durationSeconds: Int,
    val playCount: Long,
    val likeCount: Long,
    val commentCount: Int,
    val shareCount: Int,
    val thumbnailUrl: String,
    val videoPlayUrl: String,
    val createdAt: String,
    val hashtags: List<String>,
    val music: MusicInfo?
)
