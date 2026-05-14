package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignatureCheckRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("current_signature_version") val currentSignatureVersion: String,
    @SerializedName("light_engine_success_rate_24h") val lightEngineSuccessRate24h: Double?
)
