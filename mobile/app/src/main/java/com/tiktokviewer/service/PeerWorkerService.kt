package com.tiktokviewer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.tiktokviewer.domain.engine.peer.PeerWorker

class PeerWorkerService : Service() {

    private val binder = LocalBinder()
    private var peerWorker: PeerWorker? = null

    inner class LocalBinder : Binder() {
        fun getService(): PeerWorkerService = this@PeerWorkerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification("Ready to help other users")
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    fun setPeerWorker(worker: PeerWorker) {
        this.peerWorker = worker
    }

    fun updateNotification(text: String) {
        val notification = buildNotification(text)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(text: String): Notification {
        val channelId = "peer_worker_channel"
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("TikTok Viewer")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "peer_worker_channel",
            "Peer Worker",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows when helping other users find videos"
            setShowBadge(false)
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_ID = 1001
    }
}
