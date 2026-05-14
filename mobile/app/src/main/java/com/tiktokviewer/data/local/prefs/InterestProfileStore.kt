package com.tiktokviewer.data.local.prefs

import android.content.Context
import com.google.gson.Gson
import com.tiktokviewer.domain.model.InterestProfile

class InterestProfileStore(context: Context) {

    private val prefs = PreferencesManager(context)
    private val gson = Gson()

    fun save(profile: InterestProfile) {
        val json = gson.toJson(profile)
        prefs.putString(KEY_PROFILE, json)
        prefs.putLong(KEY_LAST_UPDATED, System.currentTimeMillis())
    }

    fun load(): InterestProfile? {
        val json = prefs.getString(KEY_PROFILE)
        if (json.isEmpty()) return null
        return try {
            gson.fromJson(json, InterestProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        prefs.remove(KEY_PROFILE)
        prefs.remove(KEY_LAST_UPDATED)
    }

    companion object {
        private const val KEY_PROFILE = "interest_profile"
        private const val KEY_LAST_UPDATED = "interest_profile_last_updated"
    }
}
