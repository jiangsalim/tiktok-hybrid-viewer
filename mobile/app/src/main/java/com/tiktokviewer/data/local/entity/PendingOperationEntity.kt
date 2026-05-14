package com.tiktokviewer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_operations")
data class PendingOperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val operationType: String,
    val payloadJson: String,
    val retryCount: Int = 0,
    val maxRetries: Int = 3,
    val createdAt: Long,
    val nextRetryAt: Long? = null
)
