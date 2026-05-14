package com.tiktokviewer.domain.engine.light

import com.tiktokviewer.domain.model.DeviceContext
import com.tiktokviewer.domain.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class WebApiStrategy(
    private val signatureGenerator: SignatureGenerator,
    private val videoParser: VideoParser
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    suspend fun execute(keyword: String, deviceContext: DeviceContext): Result {
        return withContext(Dispatchers.IO) {
            try {
                val baseUrl = "https://www.tiktok.com/api/search/general/full/"
                val queryUrl = "${baseUrl}?keyword=$keyword&offset=0&count=20"

                val webContext = deviceContext.copy(
                    userAgent = "Mozilla/5.0 (Linux; Android ${deviceContext.androidVersion}; " +
                            "${deviceContext.deviceModel}) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/125.0.6422.165 Mobile Safari/537.36"
                )

                val signed = signatureGenerator.signWebApi(queryUrl, webContext.userAgent)

                val signedUrl = "${queryUrl}&X-Bogus=${signed.xBogus}"

                val request = Request.Builder()
                    .url(signedUrl)
                    .addHeader("User-Agent", webContext.userAgent)
                    .addHeader("Referer", "https://www.tiktok.com/")
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", deviceContext.language)
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val body = response.body?.string() ?: ""
                    val videos = videoParser.parseWebApiResponse(body)
                    if (videos.isNotEmpty()) {
                        Result.Success(videos)
                    } else {
                        Result.Failure("web_api_empty_response")
                    }
                } else {
                    Result.Failure("web_api_http_${response.code}")
                }
            } catch (e: Exception) {
                Result.Failure("web_api_exception: ${e.message}")
            }
        }
    }

    sealed class Result {
        data class Success(val videos: List<VideoItem>) : Result()
        data class Failure(val errorCode: String) : Result()
    }
}
