package com.tiktokviewer.domain.engine.heavy

import android.webkit.WebView
import com.tiktokviewer.domain.engine.light.BehaviorSimulator
import com.tiktokviewer.domain.engine.light.DeviceSpoofer
import com.tiktokviewer.domain.engine.light.VideoParser
import com.tiktokviewer.domain.model.FailureRecord
import com.tiktokviewer.domain.model.SearchResult
import com.tiktokviewer.domain.model.SearchSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeavyEngine(
    private val deviceSpoofer: DeviceSpoofer,
    private val behaviorSimulator: BehaviorSimulator,
    private val videoParser: VideoParser,
    private val cookieJar: CookieJar
) {
    private val resourceInterceptor = ResourceInterceptor()
    private val captchaDetector = CaptchaDetector()
    private val domExtractor = DOMExtractor()

    suspend fun search(
        keyword: String,
        deviceId: String,
        webView: WebView
    ): HeavySearchResult {
        val searchId = "search_${System.currentTimeMillis()}"
        val deviceContext = deviceSpoofer.buildWebContext()

        val webViewManager = WebViewManager(resourceInterceptor, captchaDetector)

        return withContext(Dispatchers.Main) {
            try {
                webViewManager.setupWebView(
                    view = webView,
                    userAgent = deviceContext.userAgent,
                    cookies = cookieJar.loadCookies()
                )

                behaviorSimulator.preSearchDelay(1000, 3000)

                val searchUrl = "https://www.tiktok.com/search?q=$keyword"
                val startTime = System.currentTimeMillis()

                val result = webViewManager.loadAndWaitForResults(searchUrl)

                val elapsed = System.currentTimeMillis() - startTime

                when (result) {
                    is HeavyEngineResult.Success -> {
                        cookieJar.saveCookies(
                            android.webkit.CookieManager.getInstance()
                                .getCookie(".tiktok.com") ?: ""
                        )

                        val videos = if (result.responseJson != null) {
                            videoParser.parseWebApiResponse(result.responseJson)
                        } else {
                            domExtractor.extractVideos(webView)
                        }

                        if (videos.isNotEmpty()) {
                            HeavySearchResult.Success(
                                SearchResult(
                                    searchId = searchId,
                                    source = if (result.source == "api_intercept")
                                        SearchSource.HEAVY_ENGINE_INTERCEPTED_API
                                    else
                                        SearchSource.HEAVY_ENGINE_WEBVIEW,
                                    videos = videos,
                                    hasMore = videos.size >= 20,
                                    cursor = null,
                                    fetchTimeMs = elapsed
                                )
                            )
                        } else {
                            HeavySearchResult.Failure(
                                listOf(
                                    FailureRecord(
                                        searchId = searchId,
                                        deviceId = deviceId,
                                        engine = "heavy_engine",
                                        phase = result.source,
                                        errorCode = "no_videos_extracted",
                                        latencyMs = elapsed
                                    )
                                )
                            )
                        }
                    }
                    is HeavyEngineResult.Failure -> {
                        if (result.errorCode.contains("captcha")) {
                            cookieJar.clearCookies()
                        }
                        HeavySearchResult.Failure(
                            listOf(
                                FailureRecord(
                                    searchId = searchId,
                                    deviceId = deviceId,
                                    engine = "heavy_engine",
                                    phase = null,
                                    errorCode = result.errorCode,
                                    latencyMs = elapsed
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                HeavySearchResult.Failure(
                    listOf(
                        FailureRecord(
                            searchId = searchId,
                            deviceId = deviceId,
                            engine = "heavy_engine",
                            phase = null,
                            errorCode = "heavy_engine_exception: ${e.message}",
                            latencyMs = 0
                        )
                    )
                )
            }
        }
    }
}

sealed class HeavySearchResult {
    data class Success(val searchResult: SearchResult) : HeavySearchResult()
    data class Failure(val failures: List<FailureRecord>) : HeavySearchResult()
}
