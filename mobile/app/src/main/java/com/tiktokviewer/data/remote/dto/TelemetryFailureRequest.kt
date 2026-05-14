package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TelemetryFailureRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("search_id") val searchId: String,
    @SerializedName("keyword") val keyword: String?,
    @SerializedName("app_version") val appVersion: String?,
    @SerializedName("signature_version") val signatureVersion: String?,
    @SerializedName("attempts") val attempts: List<AttemptDto>
)

data class AttemptDto(
    @SerializedName("engine") val engine: String,
    @SerializedName("phase") val phase: String?,
    @SerializedName("error_code") val errorCode: String,
    @SerializedName("latency_ms") val latencyMs: Long,
    @SerializedName("timestamp") val timestamp: String
)
