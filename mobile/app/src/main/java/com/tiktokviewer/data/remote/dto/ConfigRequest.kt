package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConfigRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("app_version") val appVersion: String
)
