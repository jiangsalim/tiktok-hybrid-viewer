package com.tiktokviewer.domain.model

data class RemoteConfig(
    val lightEngine: LightEngineConfig,
    val heavyEngine: HeavyEngineConfig,
    val peerNetwork: PeerNetworkConfig,
    val serverProxy: ServerProxyConfig,
    val cache: CacheConfig,
    val telemetry: TelemetryConfig
)

data class LightEngineConfig(
    val mobileApiEnabled: Boolean,
    val webApiEnabled: Boolean,
    val htmlScrapeEnabled: Boolean,
    val requestTimeoutMs: Int,
    val preSearchDelayMinMs: Int,
    val preSearchDelayMaxMs: Int,
    val typoSimulationChance: Float
)

data class HeavyEngineConfig(
    val enabled: Boolean,
    val webviewTimeoutMs: Int,
    val maxConsecutiveFailuresBeforeCooldown: Int,
    val cooldownMinutes: Int
)

data class PeerNetworkConfig(
    val enabled: Boolean,
    val minBatteryPct: Int,
    val requireCharging: Boolean,
    val requireWifi: Boolean,
    val heartbeatIntervalMs: Int,
    val taskTimeoutMs: Int,
    val maxTasksPerHour: Int,
    val maxRequestsPerHour: Int
)

data class ServerProxyConfig(
    val enabled: Boolean,
    val requestTimeoutMs: Int
)

data class CacheConfig(
    val searchTtlMinutes: Int,
    val thumbnailMaxMb: Int,
    val videoCacheMaxMb: Int,
    val backgroundRefreshEnabled: Boolean,
    val maxCacheEntries: Int
)

data class TelemetryConfig(
    val failureReportEnabled: Boolean,
    val successReportSamplingRate: Float,
    val logPersistDays: Int
)
