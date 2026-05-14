package com.tiktokviewer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "video_bookmarks",
    indices = [
        Index(value = ["videoId"], unique = true)
    ]
)
data class VideoBookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoId: String,
    val videoJson: String,
    val thumbnailPath: String? = null,
    val videoPath: String? = null,
    val isDownloaded: Boolean = false,
    val createdAt: Long
)
