package com.tiktokviewer.domain.model

data class SignalEvent(
    val id: Long = 0,
    val sessionId: String,
    val signalType: SignalType,
    val videoId: String? = null,
    val keyword: String? = null,
    val authorUsername: String? = null,
    val hashtags: List<String>? = null,
    val musicId: String? = null,
    val watchPercent: Float? = null,
    val scrollSpeed: Float? = null,
    val extraJson: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val deviceId: String
)
