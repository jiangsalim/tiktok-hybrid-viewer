package com.tiktokviewer.domain.model

data class DeviceContext(
    val deviceId: String,
    val userAgent: String,
    val androidVersion: String,
    val deviceModel: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val language: String,
    val timezone: String,
    val tiktokVersion: String,
    val cookieSession: String? = null
)
