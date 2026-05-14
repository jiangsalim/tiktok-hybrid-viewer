package com.tiktokviewer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_cache",
    indices = [
        Index(value = ["cacheKey"], unique = true),
        Index(value = ["lastAccessed"])
    ]
)
data class SearchCacheEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cacheKey: String,
    val resultJson: String,
    val source: String? = null,
    val ttlSeconds: Int = 1800,
    val createdAt: Long,
    val lastAccessed: Long
)
