package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TelemetryResponse(
    @SerializedName("status") val status: String
)
