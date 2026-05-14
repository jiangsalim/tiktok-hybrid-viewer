package com.tiktokviewer.presentation.player

import android.app.Activity
import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import com.tiktokviewer.data.local.prefs.PreferencesManager

class PictureInPictureManager(
    private val activity: Activity,
    private val prefs: PreferencesManager
) {
    companion object {
        private const val KEY_PIP_ENABLED = "pip_enabled"
        const val ACTION_PLAY_PAUSE = "com.tiktokviewer.PIP_PLAY_PAUSE"
        const val ACTION_NEXT = "com.tiktokviewer.PIP_NEXT"
        const val ACTION_CLOSE = "com.tiktokviewer.PIP_CLOSE"
    }

    private val _isEnabled = prefs.getBoolean(KEY_PIP_ENABLED, false)
    val isEnabled: Boolean get() = _isEnabled

    fun toggle() {
        val newValue = !_isEnabled
        prefs.putBoolean(KEY_PIP_ENABLED, newValue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enterPictureInPicture() {
        if (!_isEnabled) return

        val actions = mutableListOf<RemoteAction>()

        val playPauseIntent = PendingIntent.getBroadcast(
            activity,
            0,
            Intent(ACTION_PLAY_PAUSE).setPackage(activity.packageName),
            PendingIntent.FLAG_IMMUTABLE
        )
        actions.add(
            RemoteAction(
                Icon.createWithResource(activity, android.R.drawable.ic_media_play),
                "Play/Pause",
                "Play or Pause",
                playPauseIntent
            )
        )

        val nextIntent = PendingIntent.getBroadcast(
            activity,
            1,
            Intent(ACTION_NEXT).setPackage(activity.packageName),
            PendingIntent.FLAG_IMMUTABLE
        )
        actions.add(
            RemoteAction(
                Icon.createWithResource(activity, android.R.drawable.ic_media_next),
                "Next",
                "Next video",
                nextIntent
            )
        )

        val params = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(9, 16))
            .setActions(actions)
            .build()

        activity.enterPictureInPictureMode(params)
    }
}

class PipActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            PictureInPictureManager.ACTION_PLAY_PAUSE -> {
                // Handle play/pause via callback
            }
            PictureInPictureManager.ACTION_NEXT -> {
                // Handle next video via callback
            }
            PictureInPictureManager.ACTION_CLOSE -> {
                // Handle close
            }
        }
    }
}
