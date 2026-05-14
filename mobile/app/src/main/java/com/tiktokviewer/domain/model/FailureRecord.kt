package com.tiktokviewer.domain.model

data class FailureRecord(
    val id: Long = 0,
    val searchId: String,
    val deviceId: String,
    val engine: String,
    val phase: String? = null,
    val errorCode: String,
    val contextJson: String? = null,
    val latencyMs: Long,
    val timestamp: Long = System.currentTimeMillis()
)
