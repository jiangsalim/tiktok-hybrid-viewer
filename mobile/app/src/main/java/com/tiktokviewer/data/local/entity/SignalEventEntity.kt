package com.tiktokviewer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "signal_events",
    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["signalType"]),
        Index(value = ["timestamp"]),
        Index(value = ["videoId"])
    ]
)
data class SignalEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: String,
    val signalType: String,
    val videoId: String? = null,
    val keyword: String? = null,
    val authorUsername: String? = null,
    val hashtagsJson: String? = null,
    val musicId: String? = null,
    val watchPercent: Float? = null,
    val scrollSpeed: Float? = null,
    val extraJson: String? = null,
    val timestamp: Long,
    val deviceId: String
)
