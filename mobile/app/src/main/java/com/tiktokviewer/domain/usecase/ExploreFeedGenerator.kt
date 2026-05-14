package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.remote.ApiService
import com.tiktokviewer.domain.model.ExploreCard
import com.tiktokviewer.domain.model.VideoItem

class ExploreFeedGenerator(private val apiService: ApiService) {

    suspend fun generate(deviceId: String): List<ExploreCard> {
        val cards = mutableListOf<ExploreCard>()

        try {
            val response = apiService.trendingGlobal(deviceId)
            if (response.isSuccessful) {
                val videos = response.body()?.videos ?: emptyList()
                cards.addAll(videos.map { it.toExploreCard() })
            }
        } catch (_: Exception) {}

        // Fallback: use category trending
        if (cards.isEmpty()) {
            val categories = listOf("cooking", "comedy", "tech", "sports", "music")
            for (category in categories.shuffled().take(3)) {
                try {
                    val response = apiService.trendingCategory(category, deviceId)
                    if (response.isSuccessful) {
                        val videos = response.body()?.videos ?: emptyList()
                        cards.addAll(videos.map { it.toExploreCard() })
                    }
                } catch (_: Exception) {}
            }
        }

        return cards.distinctBy { it.videoId }.shuffled()
    }

    private fun com.tiktokviewer.data.remote.dto.VideoItemDto.toExploreCard(): ExploreCard {
        return ExploreCard(
            videoId = videoId,
            thumbnailUrl = thumbnailUrl ?: "",
            creatorName = author?.displayName ?: author?.username,
            creatorEmoji = null,
            description = description,
            hashtags = hashtags,
            engagementCount = formatCount(playCount ?: 0),
            isFollowSuggestion = false
        )
    }

    private fun formatCount(count: Long): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000f)
            count >= 1_000 -> String.format("%.1fK", count / 1_000f)
            else -> count.toString()
        }
    }
}
