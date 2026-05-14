package com.tiktokviewer.domain.model

data class SearchResult(
    val searchId: String,
    val source: SearchSource,
    val videos: List<VideoItem>,
    val hasMore: Boolean,
    val cursor: String?,
    val fetchTimeMs: Long,
    val timestamp: Long = System.currentTimeMillis()
)
