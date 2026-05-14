package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerPollResponse(
    @SerializedName("status") val status: String,
    @SerializedName("task") val task: PeerTaskData? = null
)

data class PeerTaskData(
    @SerializedName("task_id") val taskId: String,
    @SerializedName("encrypted_query") val encryptedQuery: String,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("ttl_seconds") val ttlSeconds: Int?
)
