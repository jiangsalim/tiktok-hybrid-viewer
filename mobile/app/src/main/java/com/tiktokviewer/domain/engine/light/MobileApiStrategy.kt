package com.tiktokviewer.domain.engine.light

import com.tiktokviewer.domain.model.DeviceContext
import com.tiktokviewer.domain.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class MobileApiStrategy(
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
                val baseUrl = "https://api.tiktokv.com/aweme/v1/search/item/"
                val queryUrl = "${baseUrl}?keyword=$keyword&count=20&type=1&offset=0"

                val signed = signatureGenerator.signMobileApi(queryUrl, deviceContext.deviceId)

                val request = Request.Builder()
                    .url(queryUrl)
                    .addHeader("User-Agent", deviceContext.userAgent)
                    .addHeader("X-Argus", signed.xArgus)
                    .addHeader("X-Gorgon", signed.xGorgon)
                    .addHeader("X-Ladon", signed.xLadon)
                    .addHeader("X-Khronos", signed.xKhronos)
                    .addHeader("Accept-Language", deviceContext.language)
                    .addHeader("Accept", "application/json")
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val body = response.body?.string() ?: ""
                    val videos = videoParser.parseMobileApiResponse(body)
                    if (videos.isNotEmpty()) {
                        Result.Success(videos)
                    } else {
                        Result.Failure("mobile_api_empty_response")
                    }
                } else {
                    Result.Failure("mobile_api_http_${response.code}")
                }
            } catch (e: Exception) {
                Result.Failure("mobile_api_exception: ${e.message}")
            }
        }
    }

    sealed class Result {
        data class Success(val videos: List<VideoItem>) : Result()
        data class Failure(val errorCode: String) : Result()
    }
}
