package com.tiktokviewer.presentation.feed

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.tiktokviewer.domain.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VideoPlayerView(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private var currentVideoId: String? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                _playbackState.value = when (state) {
                    Player.STATE_IDLE -> PlaybackState.IDLE
                    Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                    Player.STATE_READY -> {
                        _duration.value = player.duration
                        if (player.playWhenReady) PlaybackState.PLAYING
                        else PlaybackState.PAUSED
                    }
                    Player.STATE_ENDED -> PlaybackState.ENDED
                    else -> PlaybackState.IDLE
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) _playbackState.value = PlaybackState.PLAYING
                else if (player.playbackState == Player.STATE_READY) {
                    _playbackState.value = PlaybackState.PAUSED
                }
            }
        })
    }

    fun play(video: VideoItem) {
        currentVideoId = video.videoId
        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(video.videoPlayUrl))
            .setMediaId(video.videoId)
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    fun playOrPause() {
        if (player.isPlaying) pause() else resume()
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun resume() {
        player.playWhenReady = true
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    fun getCurrentPosition(): Long = player.currentPosition

    fun getDuration(): Long = player.duration

    fun setSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }

    fun getSpeed(): Float = player.playbackParameters.speed

    fun isPlaying(): Boolean = player.isPlaying

    fun release() {
        player.release()
    }

    fun getPlayer(): ExoPlayer = player

    enum class PlaybackState {
        IDLE, BUFFERING, PLAYING, PAUSED, ENDED
    }
}
