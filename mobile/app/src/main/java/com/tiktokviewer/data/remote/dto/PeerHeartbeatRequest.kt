package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerHeartbeatRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("battery_pct") val batteryPct: Int,
    @SerializedName("is_charging") val isCharging: Boolean,
    @SerializedName("is_wifi") val isWifi: Boolean,
    @SerializedName("is_screen_off") val isScreenOff: Boolean,
    @SerializedName("network_type") val networkType: String,
    @SerializedName("current_load") val currentLoad: String
)
