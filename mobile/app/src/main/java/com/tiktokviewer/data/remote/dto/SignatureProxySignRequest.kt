package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignatureProxySignRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("url") val url: String,
    @SerializedName("method") val method: String,
    @SerializedName("timestamp") val timestamp: Long
)
