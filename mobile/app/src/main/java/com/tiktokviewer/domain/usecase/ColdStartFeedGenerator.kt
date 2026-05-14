package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.remote.ApiService
import com.tiktokviewer.domain.model.SearchResult
import com.tiktokviewer.domain.model.SearchSource
import com.tiktokviewer.domain.model.VideoItem

class ColdStartFeedGenerator(private val apiService: ApiService) {

    private val fallbackKeywords = listOf(
        "viral", "trending", "funny", "cute", "dance",
        "cooking", "music", "sports", "comedy", "lifehacks"
    )

    suspend fun generate(deviceId: String): List<VideoItem> {
        // Try trending first
        try {
            val response = apiService.trendingGlobal(deviceId)
            if (response.isSuccessful) {
                val videos = response.body()?.videos?.mapNotNull { it.toDomain() } ?: emptyList()
                if (videos.isNotEmpty()) {
                    return shuffleByCategory(videos)
                }
            }
        } catch (_: Exception) {}

        // Fallback: use diverse keywords
        val allVideos = mutableListOf<VideoItem>()
        for (keyword in fallbackKeywords.shuffled().take(5)) {
            try {
                val response = apiService.trendingCategory(keyword, deviceId)
                if (response.isSuccessful) {
                    response.body()?.videos?.mapNotNull { it.toDomain() }?.let { videos ->
                        allVideos.addAll(videos)
                    }
                }
            } catch (_: Exception) {}
        }

        return shuffleByCategory(allVideos.distinctBy { it.videoId })
    }

    private fun shuffleByCategory(videos: List<VideoItem>): List<VideoItem> {
        val byCategory = videos.groupBy { it.hashtags.firstOrNull() ?: "general" }
        val result = mutableListOf<VideoItem>()
        val keys = byCategory.keys.toList().shuffled()
        var index = 0
        while (result.size < videos.size) {
            val category = keys[index % keys.size]
            val categoryVideos = byCategory[category] ?: continue
            val videoIndex = (index / keys.size) % categoryVideos.size
            if (videoIndex < categoryVideos.size) {
                result.add(categoryVideos[videoIndex])
            }
            index++
        }
        return result
    }

    private fun com.tiktokviewer.data.remote.dto.VideoItemDto.toDomain(): VideoItem? {
        return try {
            VideoItem(
                videoId = videoId,
                description = description ?: "",
                author = com.tiktokviewer.domain.model.AuthorInfo(
                    username = author?.username ?: "",
                    displayName = author?.displayName ?: "",
                    avatarUrl = author?.avatarUrl ?: "",
                    verified = author?.verified ?: false,
                    followerCount = author?.followerCount ?: 0
                ),
                durationSeconds = durationSeconds ?: 0,
                playCount = playCount ?: 0,
                likeCount = likeCount ?: 0,
                commentCount = commentCount ?: 0,
                shareCount = shareCount ?: 0,
                thumbnailUrl = thumbnailUrl ?: "",
                videoPlayUrl = videoPlayUrl ?: "",
                createdAt = createdAt ?: "",
                hashtags = hashtags ?: emptyList(),
                music = music?.let {
                    com.tiktokviewer.domain.model.MusicInfo(
                        songName = it.songName ?: "Original Sound",
                        artistName = it.artistName,
                        musicUrl = it.musicUrl
                    )
                }
            )
        } catch (e: Exception) {
            null
        }
    }
}
