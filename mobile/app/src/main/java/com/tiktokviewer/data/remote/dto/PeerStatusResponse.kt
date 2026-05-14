package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerStatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("worker_assigned") val workerAssigned: Boolean?,
    @SerializedName("elapsed_ms") val elapsedMs: Long?,
    @SerializedName("result") val result: SearchResultDto?,
    @SerializedName("reason") val reason: String?
)
