package com.tiktokviewer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "failure_log",
    indices = [
        Index(value = ["searchId"])
    ]
)
data class FailureLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val searchId: String,
    val engine: String,
    val phase: String? = null,
    val errorCode: String,
    val contextJson: String? = null,
    val latencyMs: Long,
    val timestamp: Long
)
