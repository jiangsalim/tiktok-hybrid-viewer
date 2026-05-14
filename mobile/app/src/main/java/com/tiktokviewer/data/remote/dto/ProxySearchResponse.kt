package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProxySearchResponse(
    @SerializedName("status") val status: String,
    @SerializedName("proxy_tier_used") val proxyTierUsed: String?,
    @SerializedName("videos") val videos: List<VideoItemDto>?,
    @SerializedName("has_more") val hasMore: Boolean?,
    @SerializedName("cursor") val cursor: String?,
    @SerializedName("fetch_time_ms") val fetchTimeMs: Long?,
    @SerializedName("retry_after_ms") val retryAfterMs: Long?,
    @SerializedName("tiers_attempted") val tiersAttempted: List<String>?
)
