package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerPollRequest(
    @SerializedName("device_id") val deviceId: String
)
