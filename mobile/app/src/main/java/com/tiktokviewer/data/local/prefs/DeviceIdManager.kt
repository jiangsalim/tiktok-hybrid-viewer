package com.tiktokviewer.data.local.prefs

import android.content.Context
import java.util.UUID

class DeviceIdManager(context: Context) {

    private val prefs = PreferencesManager(context)

    fun getOrCreate(): String {
        val existing = prefs.getString(KEY_DEVICE_ID)
        if (existing.isNotEmpty()) {
            return existing
        }
        val newId = UUID.randomUUID().toString().replace("-", "")
        prefs.putString(KEY_DEVICE_ID, newId)
        return newId
    }

    fun get(): String {
        return prefs.getString(KEY_DEVICE_ID)
    }

    fun regenerate() {
        val newId = UUID.randomUUID().toString().replace("-", "")
        prefs.putString(KEY_DEVICE_ID, newId)
    }

    companion object {
        private const val KEY_DEVICE_ID = "device_id"
    }
}
