package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerRequestResponse(
    @SerializedName("status") val status: String,
    @SerializedName("task_id") val taskId: String?,
    @SerializedName("estimated_wait_ms") val estimatedWaitMs: Int?,
    @SerializedName("reason") val reason: String?
)
