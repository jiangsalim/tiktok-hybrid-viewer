package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HealthResponse(
    @SerializedName("host") val host: String,
    @SerializedName("status") val status: String,
    @SerializedName("version") val version: String?,
    @SerializedName("uptime_minutes") val uptimeMinutes: Long?,
    @SerializedName("remaining_monthly_hours") val remainingMonthlyHours: Int?,
    @SerializedName("supabase_connected") val supabaseConnected: Boolean?,
    @SerializedName("active_peers") val activePeers: Int?,
    @SerializedName("server_time") val serverTime: String?
)
