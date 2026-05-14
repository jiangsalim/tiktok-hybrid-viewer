package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.local.dao.SignalEventDao
import com.tiktokviewer.data.local.prefs.InterestProfileStore
import com.tiktokviewer.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class InterestAggregationUseCase(
    private val signalEventDao: SignalEventDao,
    private val interestProfileStore: InterestProfileStore
) {
    private val gson = Gson()

    companion object {
        val SIGNAL_WEIGHTS = mapOf(
            "SEARCH_QUERY" to 0.30f,
            "SEARCH_CLICK" to 0.25f,
            "VIDEO_WATCH_100" to 0.20f,
            "VIDEO_REWATCH" to 0.20f,
            "VIDEO_REPLAY_BUTTON" to 0.25f,
            "VIDEO_SHARE" to 0.30f,
            "VIDEO_WATCH_75" to 0.15f,
            "VIDEO_WATCH_50" to 0.10f,
            "VIDEO_WATCH_25" to 0.05f,
            "VIDEO_PAUSE" to 0.08f,
            "VIDEO_START" to 0.02f,
            "VIDEO_SKIP" to -0.15f,
            "VIDEO_EXTERNAL_LINK" to 0.10f,
            "NOT_INTERESTED" to -0.40f
        )

        val CATEGORY_KEYWORDS = mapOf(
            "cooking" to listOf("recipe", "cooking", "food", "bake", "meal", "kitchen", "eat"),
            "comedy" to listOf("funny", "comedy", "laugh", "joke", "humor", "prank", "skit"),
            "tech" to listOf("tech", "phone", "review", "gadget", "unboxing", "code", "app"),
            "sports" to listOf("sport", "football", "soccer", "basketball", "fitness", "gym", "workout"),
            "music" to listOf("music", "song", "cover", "guitar", "sing", "dance", "beat"),
            "fashion" to listOf("fashion", "outfit", "style", "makeup", "beauty", "hair", "wear"),
            "education" to listOf("learn", "tip", "tutorial", "howto", "explain", "fact"),
            "travel" to listOf("travel", "trip", "place", "view", "nature", "adventure", "explore"),
            "gaming" to listOf("game", "gaming", "play", "minecraft", "fortnite", "stream"),
            "animals" to listOf("dog", "cat", "animal", "pet", "cute", "wildlife", "bird")
        )
    }

    suspend fun aggregate(deviceId: String): InterestProfile {
        val since = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000L)
        val events = signalEventDao.getByTypeSince("VIDEO_WATCH_100", since) +
                signalEventDao.getByTypeSince("VIDEO_SHARE", since) +
                signalEventDao.getByTypeSince("SEARCH_QUERY", since) +
                signalEventDao.getByTypeSince("SEARCH_CLICK", since) +
                signalEventDao.getByTypeSince("VIDEO_REWATCH", since) +
                signalEventDao.getByTypeSince("NOT_INTERESTED", since)

        val categories = mutableMapOf<String, Float>()
        val keywords = mutableMapOf<String, Float>()
        val creators = mutableMapOf<String, CreatorAffinity>()
        val hashtags = mutableMapOf<String, Float>()
        val music = mutableMapOf<String, Float>()

        var totalWatches = 0
        var totalCompletes = 0
        var totalSkips = 0

        for (event in events) {
            val weight = SIGNAL_WEIGHTS[event.signalType] ?: 0.02f

            // Track watch stats
            if (event.signalType in listOf("VIDEO_START", "VIDEO_WATCH_25", "VIDEO_WATCH_50",
                    "VIDEO_WATCH_75", "VIDEO_WATCH_100")) {
                totalWatches++
                if (event.signalType == "VIDEO_WATCH_100") totalCompletes++
            }
            if (event.signalType == "VIDEO_SKIP") totalSkips++

            // Extract hashtags
            val tags = event.hashtagsJson?.let {
                try { gson.fromJson<List<String>>(it, object : TypeToken<List<String>>() {}.type) }
                catch (_: Exception) { null }
            } ?: emptyList()

            // Update hashtag weights
            for (tag in tags) {
                val key = tag.lowercase()
                hashtags[key] = (hashtags[key] ?: 0f) + weight
            }

            // Update category weights
            val matchedCategories = inferCategories(tags, event.keyword ?: "")
            for (cat in matchedCategories) {
                categories[cat] = (categories[cat] ?: 0f) + weight
            }

            // Update creator weights
            event.authorUsername?.let { author ->
                val existing = creators[author]
                if (existing != null) {
                    creators[author] = existing.copy(
                        weight = existing.weight + weight,
                        watches = existing.watches + 1,
                        completes = if (event.signalType == "VIDEO_WATCH_100")
                            existing.completes + 1 else existing.completes
                    )
                } else {
                    creators[author] = CreatorAffinity(
                        weight = weight,
                        watches = 1,
                        completes = if (event.signalType == "VIDEO_WATCH_100") 1 else 0
                    )
                }
            }

            // Update music
            event.musicId?.let { musicId ->
                music[musicId] = (music[musicId] ?: 0f) + weight * 0.5f
            }
        }

        // Normalize scores
        val maxCat = categories.values.maxOrNull() ?: 1f
        if (maxCat > 0) categories.entries.forEach { categories[it.key] = it.value / maxCat }

        val maxKey = keywords.values.maxOrNull() ?: 1f
        if (maxKey > 0) keywords.entries.forEach { keywords[it.key] = it.value / maxKey }

        val total = totalWatches.coerceAtLeast(1)

        val profile = InterestProfile(
            deviceId = deviceId,
            lastUpdated = System.currentTimeMillis(),
            categories = categories,
            keywords = keywords,
            creators = creators,
            hashtags = hashtags,
            music = music,
            timePatterns = TimePatterns(),
            averageSessionMinutes = 5f,
            videosPerSession = total.toFloat(),
            completionRate = totalCompletes.toFloat() / total,
            skipRate = totalSkips.toFloat() / total,
            lastDecayRun = System.currentTimeMillis()
        )

        interestProfileStore.save(profile)
        return profile
    }

    private fun inferCategories(tags: List<String>, keyword: String): List<String> {
        val matched = mutableSetOf<String>()
        val searchText = (tags + keyword).joinToString(" ").lowercase()

        for ((category, catKeywords) in CATEGORY_KEYWORDS) {
            for (kw in catKeywords) {
                if (kw in searchText) {
                    matched.add(category)
                    break
                }
            }
        }
        return matched.toList()
    }
}
