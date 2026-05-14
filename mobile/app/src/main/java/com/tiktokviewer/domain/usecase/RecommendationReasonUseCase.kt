package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.local.prefs.InterestProfileStore
import com.tiktokviewer.domain.model.VideoItem

class RecommendationReasonUseCase(
    private val interestProfileStore: InterestProfileStore
) {
    fun generateReason(video: VideoItem): RecommendationReason {
        val profile = interestProfileStore.load() ?: return RecommendationReason(
            title = "Trending content",
            details = listOf("This video is popular right now"),
            source = "trending"
        )

        val reasons = mutableListOf<String>()
        val details = mutableListOf<String>()

        // Check category match
        for ((category, weight) in profile.categories) {
            val match = video.hashtags.any { tag ->
                InterestAggregationUseCase.CATEGORY_KEYWORDS[category]?.any { kw ->
                    tag.contains(kw, ignoreCase = true)
                } == true
            }
            if (match && weight > 0.3f) {
                reasons.add("category:$category")
                details.add("Because you're interested in $category content")
            }
        }

        // Check creator match
        val creatorAffinity = profile.creators[video.author.username]
        if (creatorAffinity != null && creatorAffinity.watches >= 3) {
            reasons.add("creator:${video.author.username}")
            details.add("You've watched ${creatorAffinity.watches} videos from @${video.author.username}")
        }

        // Check hashtag match
        for (tag in video.hashtags) {
            val weight = profile.hashtags[tag.lowercase()] ?: 0f
            if (weight > 0.2f) {
                reasons.add("hashtag:$tag")
                details.add("You've watched several #$tag videos")
            }
        }

        if (details.isEmpty()) {
            details.add("Exploring new content for you")
            return RecommendationReason(
                title = "Exploring",
                details = details,
                source = "exploration"
            )
        }

        return RecommendationReason(
            title = "Because you watched",
            details = details,
            source = "personalized"
        )
    }
}

data class RecommendationReason(
    val title: String,
    val details: List<String>,
    val source: String
)
