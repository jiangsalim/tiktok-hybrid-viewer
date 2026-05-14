package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerStatusRequest(
    @SerializedName("requester_id") val requesterId: String,
    @SerializedName("task_id") val taskId: String
)
