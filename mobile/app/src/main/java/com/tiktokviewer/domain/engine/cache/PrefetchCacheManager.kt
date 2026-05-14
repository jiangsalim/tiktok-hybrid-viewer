package com.tiktokviewer.domain.engine.cache

import com.tiktokviewer.data.local.prefs.PreferencesManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tiktokviewer.domain.model.VideoItem

class PrefetchCacheManager(private val prefs: PreferencesManager) {

    private val gson = Gson()

    fun storeForYouFeed(videos: List<VideoItem>) {
        val json = gson.toJson(videos)
        prefs.putString(KEY_FOR_YOU_FEED, json)
        prefs.putLong(KEY_FOR_YOU_TIMESTAMP, System.currentTimeMillis())
    }

    fun getForYouFeed(): List<VideoItem>? {
        val json = prefs.getString(KEY_FOR_YOU_FEED)
        if (json.isEmpty()) return null

        val age = System.currentTimeMillis() - prefs.getLong(KEY_FOR_YOU_TIMESTAMP)
        if (age > MAX_FEED_AGE_MS) return null

        return try {
            val type = object : TypeToken<List<VideoItem>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    fun storeExploreFeed(videos: List<VideoItem>) {
        val json = gson.toJson(videos)
        prefs.putString(KEY_EXPLORE_FEED, json)
        prefs.putLong(KEY_EXPLORE_TIMESTAMP, System.currentTimeMillis())
    }

    fun getExploreFeed(): List<VideoItem>? {
        val json = prefs.getString(KEY_EXPLORE_FEED)
        if (json.isEmpty()) return null

        val age = System.currentTimeMillis() - prefs.getLong(KEY_EXPLORE_TIMESTAMP)
        if (age > MAX_FEED_AGE_MS) return null

        return try {
            val type = object : TypeToken<List<VideoItem>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        prefs.remove(KEY_FOR_YOU_FEED)
        prefs.remove(KEY_FOR_YOU_TIMESTAMP)
        prefs.remove(KEY_EXPLORE_FEED)
        prefs.remove(KEY_EXPLORE_TIMESTAMP)
    }

    companion object {
        private const val KEY_FOR_YOU_FEED = "prefetch_for_you"
        private const val KEY_FOR_YOU_TIMESTAMP = "prefetch_for_you_ts"
        private const val KEY_EXPLORE_FEED = "prefetch_explore"
        private const val KEY_EXPLORE_TIMESTAMP = "prefetch_explore_ts"
        private const val MAX_FEED_AGE_MS = 30 * 60 * 1000L
    }
}
