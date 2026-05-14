package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrendingResponse(
    @SerializedName("status") val status: String,
    @SerializedName("country") val country: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("videos") val videos: List<VideoItemDto>?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("expires_at") val expiresAt: String?
)
