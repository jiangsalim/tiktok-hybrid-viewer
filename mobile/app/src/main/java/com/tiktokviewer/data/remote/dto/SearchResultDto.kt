package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResultDto(
    @SerializedName("videos") val videos: List<VideoItemDto>?,
    @SerializedName("source") val source: String?,
    @SerializedName("fetch_time_ms") val fetchTimeMs: Long?,
    @SerializedName("has_more") val hasMore: Boolean?,
    @SerializedName("cursor") val cursor: String?
)
