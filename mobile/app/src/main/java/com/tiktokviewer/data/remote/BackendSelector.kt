package com.tiktokviewer.data.remote

import com.tiktokviewer.data.local.prefs.PreferencesManager

class BackendSelector(private val prefs: PreferencesManager) {

    companion object {
        const val PRIMARY_URL = "https://tiktok-viewer-api.onrender.com/api/v1"
        const val BACKUP_URL = "https://tiktok-viewer-api.oraclecloud.com/api/v1"
        private const val KEY_ACTIVE_BACKEND = "active_backend"
        private const val KEY_SWITCHED_AT = "backend_switched_at"
        private const val BACKEND_PRIMARY = "primary"
        private const val BACKEND_BACKUP = "backup"
    }

    fun getBackendUrl(): String {
        val active = prefs.getString(KEY_ACTIVE_BACKEND, BACKEND_PRIMARY)
        return if (active == BACKEND_BACKUP) BACKUP_URL else PRIMARY_URL
    }

    fun switchToBackup() {
        prefs.putString(KEY_ACTIVE_BACKEND, BACKEND_BACKUP)
        prefs.putLong(KEY_SWITCHED_AT, System.currentTimeMillis())
    }

    fun switchToPrimary() {
        prefs.putString(KEY_ACTIVE_BACKEND, BACKEND_PRIMARY)
        prefs.putLong(KEY_SWITCHED_AT, System.currentTimeMillis())
    }

    fun isOnBackup(): Boolean {
        return prefs.getString(KEY_ACTIVE_BACKEND, BACKEND_PRIMARY) == BACKEND_BACKUP
    }

    fun timeSinceLastSwitchMs(): Long {
        val switchedAt = prefs.getLong(KEY_SWITCHED_AT)
        if (switchedAt == 0L) return Long.MAX_VALUE
        return System.currentTimeMillis() - switchedAt
    }
}
