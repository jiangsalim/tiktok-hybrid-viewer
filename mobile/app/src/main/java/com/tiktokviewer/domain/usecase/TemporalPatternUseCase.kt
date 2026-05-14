package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.local.dao.SignalEventDao
import com.tiktokviewer.data.local.prefs.InterestProfileStore
import com.tiktokviewer.domain.model.TimePatterns
import java.util.*

class TemporalPatternUseCase(
    private val signalEventDao: SignalEventDao,
    private val interestProfileStore: InterestProfileStore
) {
    companion object {
        private val TIME_BLOCKS = mapOf(
            "morning" to (6..11),
            "afternoon" to (12..16),
            "evening" to (17..21),
            "night" to listOf(22, 23, 0, 1, 2, 3, 4, 5)
        )
    }

    suspend fun analyze() {
        val profile = interestProfileStore.load() ?: return
        val since = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000L)
        val events = signalEventDao.getByTypeSince("VIDEO_WATCH_100", since)

        val blockCategories = mutableMapOf<String, MutableMap<String, Int>>()
        for (block in TIME_BLOCKS.keys) {
            blockCategories[block] = mutableMapOf()
        }

        val calendar = Calendar.getInstance()

        for (event in events) {
            calendar.timeInMillis = event.timestamp
            val hour = calendar.get(Calendar.HOUR_OF_DAY)

            val block = TIME_BLOCKS.entries.find { (_, hours) ->
                hour in hours
            }?.key ?: continue

            val tags = event.hashtagsJson?.let { json ->
                try {
                    com.google.gson.Gson().fromJson<List<String>>(
                        json,
                        object : com.google.gson.reflect.TypeToken<List<String>>() {}.type
                    )
                } catch (_: Exception) { null }
            } ?: emptyList()

            for (tag in tags) {
                val category = inferCategory(tag)
                if (category != null) {
                    val counts = blockCategories[block]!!
                    counts[category] = (counts[category] ?: 0) + 1
                }
            }
        }

        val patterns = TimePatterns(
            morning = getDominantCategory(blockCategories["morning"]),
            afternoon = getDominantCategory(blockCategories["afternoon"]),
            evening = getDominantCategory(blockCategories["evening"]),
            night = getDominantCategory(blockCategories["night"])
        )

        interestProfileStore.save(profile.copy(timePatterns = patterns))
    }

    private fun getDominantCategory(counts: Map<String, Int>?): String? {
        if (counts.isNullOrEmpty()) return null
        return counts.entries.maxByOrNull { it.value }?.key
    }

    private fun inferCategory(tag: String): String? {
        val lower = tag.lowercase()
        for ((category, keywords) in InterestAggregationUseCase.CATEGORY_KEYWORDS) {
            for (kw in keywords) {
                if (kw in lower) return category
            }
        }
        return null
    }

    fun getCurrentTimeBlock(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return TIME_BLOCKS.entries.find { (_, hours) -> hour in hours }?.key ?: "evening"
    }
}
