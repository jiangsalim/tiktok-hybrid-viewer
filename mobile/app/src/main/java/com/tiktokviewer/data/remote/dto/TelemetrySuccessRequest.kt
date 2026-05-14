package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TelemetrySuccessRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("engine") val engine: String,
    @SerializedName("phase_used") val phaseUsed: String?,
    @SerializedName("keyword") val keyword: String?,
    @SerializedName("latency_ms") val latencyMs: Long,
    @SerializedName("results_count") val resultsCount: Int?,
    @SerializedName("timestamp") val timestamp: String
)
