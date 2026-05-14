package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerHeartbeatResponse(
    @SerializedName("status") val status: String,
    @SerializedName("queued_tasks") val queuedTasks: Int?,
    @SerializedName("heartbeat_interval_ms") val heartbeatIntervalMs: Int?
)
