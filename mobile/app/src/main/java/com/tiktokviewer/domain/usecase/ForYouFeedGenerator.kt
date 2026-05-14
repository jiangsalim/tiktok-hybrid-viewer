package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.local.prefs.InterestProfileStore
import com.tiktokviewer.domain.model.InterestProfile
import com.tiktokviewer.domain.model.VideoItem
import kotlin.random.Random

class ForYouFeedGenerator(
    private val interestProfileStore: InterestProfileStore,
    private val searchOrchestrator: SearchOrchestrator,
    private val coldStartFeedGenerator: ColdStartFeedGenerator
) {
    suspend fun generateFeed(deviceId: String, webView: android.webkit.WebView? = null): List<VideoItem> {
        val profile = interestProfileStore.load()

        return if (profile != null && hasEnoughData(profile)) {
            generatePersonalizedFeed(profile, deviceId, webView)
        } else {
            coldStartFeedGenerator.generate(deviceId)
        }
    }

    private fun hasEnoughData(profile: InterestProfile): Boolean {
        return profile.categories.isNotEmpty() && profile.videosPerSession > 2
    }

    private suspend fun generatePersonalizedFeed(
        profile: InterestProfile,
        deviceId: String,
        webView: android.webkit.WebView?
    ): List<VideoItem> {
        val allVideos = mutableListOf<VideoItem>()
        val seenIds = mutableSetOf<String>()

        // Search top categories
        val topCategories = profile.categories.entries
            .sortedByDescending { it.value }
            .take(3)

        for ((category, _) in topCategories) {
            val result = searchOrchestrator.search(category, deviceId, webView)
            if (result is OrchestratorResult.Success) {
                for (video in result.searchResult.videos) {
                    if (seenIds.add(video.videoId)) {
                        allVideos.add(video)
                    }
                }
            }
        }

        // Search top keywords
        val topKeywords = profile.keywords.entries
            .sortedByDescending { it.value }
            .take(3)

        for ((keyword, _) in topKeywords) {
            val result = searchOrchestrator.search(keyword, deviceId, webView)
            if (result is OrchestratorResult.Success) {
                for (video in result.searchResult.videos) {
                    if (seenIds.add(video.videoId)) {
                        allVideos.add(video)
                    }
                }
            }
        }

        // Search top creators
        val topCreators = profile.creators.entries
            .sortedByDescending { it.value.weight }
            .take(2)

        for ((creator, _) in topCreators) {
            val result = searchOrchestrator.search(creator, deviceId, webView)
            if (result is OrchestratorResult.Success) {
                for (video in result.searchResult.videos) {
                    if (seenIds.add(video.videoId)) {
                        allVideos.add(video)
                    }
                }
            }
        }

        // Add exploration (15% random categories)
        if (Random.nextInt(100) < 15) {
            val explorationCategories = listOf(
                "travel", "gaming", "fashion", "education", "animals",
                "tech", "diy", "art", "fitness", "food"
            )
            val randomCategory = explorationCategories.random()
            val result = searchOrchestrator.search(randomCategory, deviceId, webView)
            if (result is OrchestratorResult.Success) {
                for (video in result.searchResult.videos) {
                    if (seenIds.add(video.videoId)) {
                        allVideos.add(video)
                    }
                }
            }
        }

        return allVideos.shuffled()
    }
}
