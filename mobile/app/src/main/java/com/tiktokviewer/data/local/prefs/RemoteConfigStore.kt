package com.tiktokviewer.data.local.prefs

import android.content.Context
import com.google.gson.Gson
import com.tiktokviewer.domain.model.RemoteConfig

class RemoteConfigStore(context: Context) {

    private val prefs = PreferencesManager(context)
    private val gson = Gson()

    fun save(config: RemoteConfig) {
        val json = gson.toJson(config)
        prefs.putString(KEY_CONFIG, json)
        prefs.putLong(KEY_FETCHED_AT, System.currentTimeMillis())
    }

    fun load(): RemoteConfig? {
        val json = prefs.getString(KEY_CONFIG)
        if (json.isEmpty()) return null
        return try {
            gson.fromJson(json, RemoteConfig::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun isExpired(ttlMinutes: Int = 60): Boolean {
        val fetchedAt = prefs.getLong(KEY_FETCHED_AT)
        if (fetchedAt == 0L) return true
        val ageMs = System.currentTimeMillis() - fetchedAt
        return ageMs > ttlMinutes * 60 * 1000L
    }

    companion object {
        private const val KEY_CONFIG = "remote_config"
        private const val KEY_FETCHED_AT = "remote_config_fetched_at"
    }
}
