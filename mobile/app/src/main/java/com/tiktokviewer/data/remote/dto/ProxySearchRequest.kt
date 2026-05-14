package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProxySearchRequest(
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("keyword") val keyword: String,
    @SerializedName("cursor") val cursor: String? = null,
    @SerializedName("count") val count: Int = 20
)
