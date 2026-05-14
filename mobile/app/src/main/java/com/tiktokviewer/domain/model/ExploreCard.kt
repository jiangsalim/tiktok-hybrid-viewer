package com.tiktokviewer.domain.model

data class ExploreCard(
    val videoId: String,
    val thumbnailUrl: String,
    val creatorName: String?,
    val creatorEmoji: String?,
    val description: String?,
    val hashtags: List<String>?,
    val engagementCount: String,
    val isFollowSuggestion: Boolean = false
)
