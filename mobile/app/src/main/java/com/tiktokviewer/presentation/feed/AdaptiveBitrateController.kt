package com.tiktokviewer.presentation.feed

import com.tiktokviewer.data.remote.NetworkMonitor
import com.tiktokviewer.data.remote.ConnectionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdaptiveBitrateController(
    private val networkMonitor: NetworkMonitor,
    private val playerView: VideoPlayerView
) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentQuality = VideoQuality.HIGH

    init {
        scope.launch {
            networkMonitor.networkStatus.collect { status ->
                when (status) {
                    is NetworkMonitor.NetworkStatus.Available -> {
                        adjustQuality(status.connectionType)
                    }
                    is NetworkMonitor.NetworkStatus.Unavailable -> {
                        currentQuality = VideoQuality.LOW
                    }
                }
            }
        }
    }

    private fun adjustQuality(connectionType: ConnectionType) {
        val newQuality = when (connectionType) {
            ConnectionType.WiFi -> VideoQuality.HIGH
            ConnectionType.Ethernet -> VideoQuality.HIGH
            ConnectionType.Cellular -> VideoQuality.MEDIUM
            ConnectionType.Unknown -> VideoQuality.LOW
        }

        if (newQuality != currentQuality) {
            currentQuality = newQuality
            applyQuality()
        }
    }

    private fun applyQuality() {
        when (currentQuality) {
            VideoQuality.LOW -> playerView.getPlayer().playWhenReady = true
            VideoQuality.MEDIUM -> playerView.getPlayer().playWhenReady = true
            VideoQuality.HIGH -> playerView.getPlayer().playWhenReady = true
        }
    }

    fun getCurrentQuality(): VideoQuality = currentQuality

    enum class VideoQuality {
        LOW, MEDIUM, HIGH
    }
}
