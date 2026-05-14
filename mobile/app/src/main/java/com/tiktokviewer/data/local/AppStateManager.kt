package com.tiktokviewer.data.local

import android.content.Context
import com.tiktokviewer.data.local.prefs.PreferencesManager
import com.google.gson.Gson

class AppStateManager(context: Context) {

    private val prefs = PreferencesManager(context)
    private val gson = Gson()

    data class AppState(
        val currentScreen: String = "for_you",
        val searchKeyword: String? = null,
        val searchResultsPage: Int = 0,
        val scrollPositionY: Int = 0,
        val playingVideoId: String? = null,
        val playingPositionMs: Long = 0,
        val forYouFeedPage: Int = 0,
        val feedVideoIds: List<String> = emptyList(),
        val timestamp: Long = System.currentTimeMillis()
    )

    fun saveState(state: AppState) {
        val json = gson.toJson(state)
        prefs.putString(KEY_APP_STATE, json)
    }

    fun restoreState(): AppState? {
        val json = prefs.getString(KEY_APP_STATE)
        if (json.isEmpty()) return null
        return try {
            val state = gson.fromJson(json, AppState::class.java)
            val age = System.currentTimeMillis() - state.timestamp
            if (age > MAX_STATE_AGE_MS) null else state
        } catch (e: Exception) {
            null
        }
    }

    fun clearState() {
        prefs.remove(KEY_APP_STATE)
    }

    companion object {
        private const val KEY_APP_STATE = "app_state_json"
        private const val MAX_STATE_AGE_MS = 15 * 60 * 1000L
    }
}
