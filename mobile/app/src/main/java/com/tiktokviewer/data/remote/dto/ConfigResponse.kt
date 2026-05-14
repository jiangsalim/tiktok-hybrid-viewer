package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    @SerializedName("config") val config: RemoteConfigDto
)

data class RemoteConfigDto(
    @SerializedName("light_engine") val lightEngine: LightEngineConfigDto?,
    @SerializedName("heavy_engine") val heavyEngine: HeavyEngineConfigDto?,
    @SerializedName("peer_network") val peerNetwork: PeerNetworkConfigDto?,
    @SerializedName("server_proxy") val serverProxy: ServerProxyConfigDto?,
    @SerializedName("cache") val cache: CacheConfigDto?,
    @SerializedName("telemetry") val telemetry: TelemetryConfigDto?,
    @SerializedName("features") val features: FeaturesConfigDto?
)

data class LightEngineConfigDto(
    @SerializedName("mobile_api_enabled") val mobileApiEnabled: Boolean?,
    @SerializedName("web_api_enabled") val webApiEnabled: Boolean?,
    @SerializedName("html_scrape_enabled") val htmlScrapeEnabled: Boolean?,
    @SerializedName("request_timeout_ms") val requestTimeoutMs: Int?,
    @SerializedName("pre_search_delay_min_ms") val preSearchDelayMinMs: Int?,
    @SerializedName("pre_search_delay_max_ms") val preSearchDelayMaxMs: Int?,
    @SerializedName("typo_simulation_chance") val typoSimulationChance: Double?
)

data class HeavyEngineConfigDto(
    @SerializedName("enabled") val enabled: Boolean?,
    @SerializedName("webview_timeout_ms") val webviewTimeoutMs: Int?,
    @SerializedName("max_consecutive_failures_before_cooldown") val maxConsecutiveFailures: Int?,
    @SerializedName("cooldown_minutes") val cooldownMinutes: Int?
)

data class PeerNetworkConfigDto(
    @SerializedName("enabled") val enabled: Boolean?,
    @SerializedName("min_battery_pct") val minBatteryPct: Int?,
    @SerializedName("require_charging") val requireCharging: Boolean?,
    @SerializedName("require_wifi") val requireWifi: Boolean?,
    @SerializedName("heartbeat_interval_ms") val heartbeatIntervalMs: Int?,
    @SerializedName("task_timeout_ms") val taskTimeoutMs: Int?,
    @SerializedName("max_tasks_per_hour") val maxTasksPerHour: Int?,
    @SerializedName("max_requests_per_hour") val maxRequestsPerHour: Int?
)

data class ServerProxyConfigDto(
    @SerializedName("enabled") val enabled: Boolean?,
    @SerializedName("request_timeout_ms") val requestTimeoutMs: Int?
)

data class CacheConfigDto(
    @SerializedName("search_ttl_minutes") val searchTtlMinutes: Int?,
    @SerializedName("thumbnail_max_mb") val thumbnailMaxMb: Int?,
    @SerializedName("video_cache_max_mb") val videoCacheMaxMb: Int?,
    @SerializedName("background_refresh_enabled") val backgroundRefreshEnabled: Boolean?,
    @SerializedName("max_cache_entries") val maxCacheEntries: Int?
)

data class TelemetryConfigDto(
    @SerializedName("failure_report_enabled") val failureReportEnabled: Boolean?,
    @SerializedName("success_report_sampling_rate") val successReportSamplingRate: Double?,
    @SerializedName("log_persist_days") val logPersistDays: Int?
)

data class FeaturesConfigDto(
    @SerializedName("for_you") val forYou: Boolean?,
    @SerializedName("explore") val explore: Boolean?,
    @SerializedName("following") val following: Boolean?,
    @SerializedName("live") val live: Boolean?,
    @SerializedName("friends_tab") val friendsTab: Boolean?,
    @SerializedName("create_button") val createButton: Boolean?,
    @SerializedName("account_linking") val accountLinking: Boolean?
)
