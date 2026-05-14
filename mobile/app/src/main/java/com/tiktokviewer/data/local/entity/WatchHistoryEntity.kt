package com.tiktokviewer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "watch_history",
    indices = [
        Index(value = ["videoId"]),
        Index(value = ["watchedAt"])
    ]
)
data class WatchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoId: String,
    val videoJson: String,
    val watchDurationMs: Long? = null,
    val watchPercent: Float? = null,
    val completed: Boolean = false,
    val watchedAt: Long
)
