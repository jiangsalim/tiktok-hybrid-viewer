package com.tiktokviewer.domain.engine.peer

import android.content.Context
import android.webkit.WebView
import com.tiktokviewer.data.remote.ApiService
import com.tiktokviewer.data.remote.dto.PeerHeartbeatRequest
import com.tiktokviewer.data.remote.dto.PeerPollRequest
import com.tiktokviewer.data.remote.dto.PeerSubmitRequest
import com.tiktokviewer.domain.usecase.SearchOrchestrator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PeerWorker(
    private val context: Context,
    private val apiService: ApiService,
    private val searchOrchestrator: SearchOrchestrator,
    private val encryptionModule: EncryptionModule,
    private val peerKeyManager: PeerKeyManager
) {
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _currentTask = MutableStateFlow<String?>(null)
    val currentTask: StateFlow<String?> = _currentTask

    private var job: Job? = null
    private var webView: WebView? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var deviceId: String = ""
    private var batteryPct: Int = 100
    private var isCharging: Boolean = false
    private var isWifi: Boolean = false
    private var isScreenOff: Boolean = false

    fun start(deviceId: String) {
        if (_isRunning.value) return
        this.deviceId = deviceId
        _isRunning.value = true
        job = scope.launch {
            while (isActive && _isRunning.value) {
                try {
                    sendHeartbeat()
                    delay(30000)
                } catch (e: Exception) {
                    delay(5000)
                }
            }
        }
        scope.launch {
            while (isActive && _isRunning.value) {
                try {
                    checkForWork()
                    delay(5000)
                } catch (e: Exception) {
                    delay(5000)
                }
            }
        }
    }

    fun stop() {
        _isRunning.value = false
        job?.cancel()
        _currentTask.value = null
    }

    fun updateConditions(battery: Int, charging: Boolean, wifi: Boolean, screenOff: Boolean) {
        this.batteryPct = battery
        this.isCharging = charging
        this.isWifi = wifi
        this.isScreenOff = screenOff
    }

    fun canWork(): Boolean {
        return batteryPct >= 50 && isCharging && isWifi
    }

    private suspend fun sendHeartbeat() {
        if (!canWork()) return
        apiService.peerHeartbeat(
            deviceId = deviceId,
            request = PeerHeartbeatRequest(
                deviceId = deviceId,
                batteryPct = batteryPct,
                isCharging = isCharging,
                isWifi = isWifi,
                isScreenOff = isScreenOff,
                networkType = if (isWifi) "wifi" else "cellular",
                currentLoad = if (_currentTask.value != null) "busy" else "idle"
            )
        )
    }

    private suspend fun checkForWork() {
        if (!canWork()) return
        if (_currentTask.value != null) return

        val response = apiService.peerPoll(
            deviceId = deviceId,
            request = PeerPollRequest(deviceId)
        )

        if (response.isSuccessful) {
            val body = response.body()
            if (body?.status == "task_available" && body.task != null) {
                processTask(body.task.taskId, body.task.encryptedQuery)
            }
        }
    }

    private suspend fun processTask(taskId: String, encryptedQuery: String) {
        _currentTask.value = taskId
        try {
            val key = peerKeyManager.getNetworkKey()
            val keyword = encryptionModule.decrypt(encryptedQuery, key) ?: run {
                submitFailure(taskId, "decryption_failed")
                return
            }

            val result = withContext(Dispatchers.Main) {
                val wv = WebView(context)
                webView = wv
                try {
                    searchOrchestrator.search(keyword, deviceId, wv)
                } finally {
                    wv.destroy()
                    webView = null
                }
            }

            when (result) {
                is com.tiktokviewer.domain.usecase.OrchestratorResult.Success -> {
                    val resultJson = com.google.gson.Gson().toJson(result.searchResult)
                    apiService.peerSubmit(
                        deviceId = deviceId,
                        request = PeerSubmitRequest(
                            deviceId = deviceId,
                            taskId = taskId,
                            status = "completed",
                            result = com.tiktokviewer.data.remote.dto.SearchResultDto(
                                videos = result.searchResult.videos.map { it.toDto() },
                                source = result.searchResult.source.name,
                                fetchTimeMs = result.searchResult.fetchTimeMs,
                                hasMore = result.searchResult.hasMore,
                                cursor = result.searchResult.cursor
                            )
                        )
                    )
                }
                else -> {
                    submitFailure(taskId, "search_failed")
                }
            }
        } catch (e: Exception) {
            submitFailure(taskId, "exception: ${e.message}")
        } finally {
            _currentTask.value = null
        }
    }

    private suspend fun submitFailure(taskId: String, errorCode: String) {
        try {
            apiService.peerSubmit(
                deviceId = deviceId,
                request = PeerSubmitRequest(
                    deviceId = deviceId,
                    taskId = taskId,
                    status = "failed",
                    errorCode = errorCode
                )
            )
        } catch (_: Exception) {}
    }

    private fun com.tiktokviewer.domain.model.VideoItem.toDto(): com.tiktokviewer.data.remote.dto.VideoItemDto {
        return com.tiktokviewer.data.remote.dto.VideoItemDto(
            videoId = videoId,
            description = description,
            author = com.tiktokviewer.data.remote.dto.AuthorDto(
                username = author.username,
                displayName = author.displayName,
                verified = author.verified,
                avatarUrl = author.avatarUrl,
                followerCount = author.followerCount
            ),
            durationSeconds = durationSeconds,
            playCount = playCount,
            likeCount = likeCount,
            commentCount = commentCount,
            shareCount = shareCount,
            thumbnailUrl = thumbnailUrl,
            videoPlayUrl = videoPlayUrl,
            createdAt = createdAt,
            hashtags = hashtags,
            music = music?.let {
                com.tiktokviewer.data.remote.dto.MusicDto(
                    songName = it.songName,
                    artistName = it.artistName,
                    musicUrl = it.musicUrl
                )
            }
        )
    }
}
