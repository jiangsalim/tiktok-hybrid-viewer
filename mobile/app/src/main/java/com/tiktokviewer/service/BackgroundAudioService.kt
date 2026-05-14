package com.tiktokviewer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.tiktokviewer.data.local.prefs.PreferencesManager

class BackgroundAudioService : Service() {

    private val binder = LocalBinder()
    private var isAudioEnabled = false

    inner class LocalBinder : Binder() {
        fun getService(): BackgroundAudioService = this@BackgroundAudioService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification("Audio playing in background")
        startForeground(NOTIFICATION_ID, notification)
        isAudioEnabled = true
        return START_STICKY
    }

    fun setAudioEnabled(enabled: Boolean) {
        isAudioEnabled = enabled
        if (!enabled) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    fun updateNotification(text: String) {
        val notification = buildNotification(text)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(text: String): Notification {
        val channelId = "background_audio_channel"
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("TikTok Viewer")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "background_audio_channel",
            "Background Audio",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows when audio continues playing in background"
            setShowBadge(false)
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_ID = 1002
        private const val KEY_BG_AUDIO = "background_audio_enabled"
    }
}
