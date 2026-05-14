package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerSubmitRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("task_id") val taskId: String,
    @SerializedName("status") val status: String,
    @SerializedName("result") val result: SearchResultDto? = null,
    @SerializedName("error_code") val errorCode: String? = null
)
