package com.tiktokviewer.domain.model

data class ProxyState(
    val id: String,
    val tier: ProxyTier,
    val ipAddress: String,
    val port: Int,
    val protocol: String,
    val status: ProxyStatus,
    val consecutiveFailures: Int = 0,
    val cooldownUntil: Long? = null,
    val lastUsed: Long? = null,
    val totalRequests: Int = 0,
    val totalSuccesses: Int = 0,
    val successRate: Float = 0f
)
