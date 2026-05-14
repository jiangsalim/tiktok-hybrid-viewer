package com.tiktokviewer.domain.engine.heavy

import android.webkit.WebView
import com.tiktokviewer.domain.model.AuthorInfo
import com.tiktokviewer.domain.model.MusicInfo
import com.tiktokviewer.domain.model.VideoItem
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DOMExtractor {

    suspend fun extractVideos(webView: WebView): List<VideoItem> {
        return suspendCancellableCoroutine { continuation ->
            val jsCode = """
                (function() {
                    try {
                        var videos = [];
                        var items = document.querySelectorAll('[data-e2e="search-video-item"], [data-e2e="recommend-video-item"]');
                        
                        items.forEach(function(item) {
                            var videoId = item.getAttribute('data-video-id') || '';
                            var desc = item.querySelector('[data-e2e="video-desc"]')?.textContent || '';
                            var authorName = item.querySelector('[data-e2e="video-author-uniqueid"]')?.textContent || '';
                            var authorDisplay = item.querySelector('[data-e2e="video-author-nickname"]')?.textContent || '';
                            var playCount = item.querySelector('[data-e2e="video-views"]')?.textContent || '0';
                            
                            videos.push({
                                videoId: videoId,
                                description: desc,
                                authorName: authorName,
                                authorDisplay: authorDisplay,
                                playCount: playCount
                            });
                        });
                        
                        return JSON.stringify(videos);
                    } catch(e) {
                        return JSON.stringify([]);
                    }
                })();
            """.trimIndent()

            webView.evaluateJavascript(jsCode) { result ->
                val videos = try {
                    val cleanResult = result.trim('"').replace("\\\"", "\"")
                    if (cleanResult == "null" || cleanResult.isEmpty()) {
                        emptyList()
                    } else {
                        parseJsonArray(cleanResult)
                    }
                } catch (e: Exception) {
                    emptyList()
                }
                continuation.resume(videos)
            }
        }
    }

    private fun parseJsonArray(json: String): List<VideoItem> {
        val videos = mutableListOf<VideoItem>()
        try {
            val parser = org.json.JSONArray(json)
            for (i in 0 until parser.length()) {
                val obj = parser.getJSONObject(i)
                videos.add(
                    VideoItem(
                        videoId = obj.optString("videoId", ""),
                        description = obj.optString("description", ""),
                        author = AuthorInfo(
                            username = obj.optString("authorName", ""),
                            displayName = obj.optString("authorDisplay", ""),
                            avatarUrl = "",
                            verified = false,
                            followerCount = 0
                        ),
                        durationSeconds = 0,
                        playCount = parseCount(obj.optString("playCount", "0")),
                        likeCount = 0,
                        commentCount = 0,
                        shareCount = 0,
                        thumbnailUrl = "",
                        videoPlayUrl = "",
                        createdAt = "",
                        hashtags = emptyList(),
                        music = null
                    )
                )
            }
        } catch (e: Exception) {
            // Fall through
        }
        return videos
    }

    private fun parseCount(text: String): Long {
        return try {
            val cleaned = text.trim()
                .replace("K", "000")
                .replace("k", "000")
                .replace("M", "000000")
                .replace("m", "000000")
                .replace(",", "")
                .replace(".", "")
            cleaned.toLongOrNull() ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}
