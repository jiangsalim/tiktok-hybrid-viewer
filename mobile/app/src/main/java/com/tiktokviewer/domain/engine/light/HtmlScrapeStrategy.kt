package com.tiktokviewer.domain.engine.light

import com.tiktokviewer.domain.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class HtmlScrapeStrategy(
    private val videoParser: VideoParser
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun execute(keyword: String, userAgent: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val searchUrl = "https://www.tiktok.com/search?q=$keyword"

                val request = Request.Builder()
                    .url(searchUrl)
                    .addHeader("User-Agent", userAgent)
                    .addHeader("Referer", "https://www.tiktok.com/")
                    .addHeader("Accept", "text/html,application/xhtml+xml")
                    .addHeader("Accept-Language", "en-US,en;q=0.9")
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val html = response.body?.string() ?: ""
                    val videos = videoParser.parseHtmlEmbeddedJson(html)
                    if (videos.isNotEmpty()) {
                        Result.Success(videos)
                    } else {
                        Result.Failure("html_scrape_no_data_found")
                    }
                } else {
                    Result.Failure("html_scrape_http_${response.code}")
                }
            } catch (e: Exception) {
                Result.Failure("html_scrape_exception: ${e.message}")
            }
        }
    }

    sealed class Result {
        data class Success(val videos: List<VideoItem>) : Result()
        data class Failure(val errorCode: String) : Result()
    }
}
