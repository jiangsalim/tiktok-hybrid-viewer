package com.tiktokviewer.domain.model

data class WorkerState(
    val deviceId: String,
    val status: WorkerStatus,
    val lastHeartbeat: Long,
    val batteryPct: Int,
    val isCharging: Boolean,
    val isWifi: Boolean,
    val isScreenOff: Boolean,
    val networkType: String,
    val successRate: Float,
    val avgResponseTimeMs: Int,
    val totalTasksCompleted: Int,
    val totalTasksFailed: Int,
    val createdAt: Long = System.currentTimeMillis()
)
