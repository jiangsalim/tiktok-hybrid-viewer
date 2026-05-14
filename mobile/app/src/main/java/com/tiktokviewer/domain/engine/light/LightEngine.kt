package com.tiktokviewer.domain.engine.light

import com.tiktokviewer.domain.model.FailureRecord
import com.tiktokviewer.domain.model.SearchResult
import com.tiktokviewer.domain.model.SearchSource
import com.tiktokviewer.domain.model.VideoItem

class LightEngine(
    private val mobileApiStrategy: MobileApiStrategy,
    private val webApiStrategy: WebApiStrategy,
    private val htmlScrapeStrategy: HtmlScrapeStrategy,
    private val deviceSpoofer: DeviceSpoofer,
    private val behaviorSimulator: BehaviorSimulator
) {
    suspend fun search(keyword: String, deviceId: String): LightEngineResult {
        val failures = mutableListOf<FailureRecord>()
        val searchId = "search_${System.currentTimeMillis()}"
        val deviceContext = deviceSpoofer.buildContext(deviceId)

        // Phase 1: Mobile API
        behaviorSimulator.preSearchDelay()
        behaviorSimulator.simulateTyping(keyword)

        val mobileStart = System.currentTimeMillis()
        when (val result = mobileApiStrategy.execute(keyword, deviceContext)) {
            is MobileApiStrategy.Result.Success -> {
                return LightEngineResult.Success(
                    SearchResult(
                        searchId = searchId,
                        source = SearchSource.LIGHT_ENGINE_MOBILE_API,
                        videos = result.videos,
                        hasMore = result.videos.size >= 20,
                        cursor = null,
                        fetchTimeMs = System.currentTimeMillis() - mobileStart
                    )
                )
            }
            is MobileApiStrategy.Result.Failure -> {
                failures.add(
                    FailureRecord(
                        searchId = searchId,
                        deviceId = deviceId,
                        engine = "light_engine",
                        phase = "mobile_api",
                        errorCode = result.errorCode,
                        latencyMs = System.currentTimeMillis() - mobileStart
                    )
                )
            }
        }

        // Phase 2: Web API
        behaviorSimulator.randomDelay(500, 1500)
        val webContext = deviceSpoofer.buildWebContext()

        val webStart = System.currentTimeMillis()
        when (val result = webApiStrategy.execute(keyword, webContext)) {
            is WebApiStrategy.Result.Success -> {
                return LightEngineResult.Success(
                    SearchResult(
                        searchId = searchId,
                        source = SearchSource.LIGHT_ENGINE_WEB_API,
                        videos = result.videos,
                        hasMore = result.videos.size >= 20,
                        cursor = null,
                        fetchTimeMs = System.currentTimeMillis() - webStart
                    )
                )
            }
            is WebApiStrategy.Result.Failure -> {
                failures.add(
                    FailureRecord(
                        searchId = searchId,
                        deviceId = deviceId,
                        engine = "light_engine",
                        phase = "web_api",
                        errorCode = result.errorCode,
                        latencyMs = System.currentTimeMillis() - webStart
                    )
                )
            }
        }

        // Phase 3: HTML Scrape
        behaviorSimulator.randomDelay(1000, 3000)

        val htmlStart = System.currentTimeMillis()
        when (val result = htmlScrapeStrategy.execute(
            keyword,
            deviceSpoofer.getWebUserAgent()
        )) {
            is HtmlScrapeStrategy.Result.Success -> {
                return LightEngineResult.Success(
                    SearchResult(
                        searchId = searchId,
                        source = SearchSource.LIGHT_ENGINE_HTML_SCRAPE,
                        videos = result.videos,
                        hasMore = result.videos.size >= 20,
                        cursor = null,
                        fetchTimeMs = System.currentTimeMillis() - htmlStart
                    )
                )
            }
            is HtmlScrapeStrategy.Result.Failure -> {
                failures.add(
                    FailureRecord(
                        searchId = searchId,
                        deviceId = deviceId,
                        engine = "light_engine",
                        phase = "html_scrape",
                        errorCode = result.errorCode,
                        latencyMs = System.currentTimeMillis() - htmlStart
                    )
                )
            }
        }

        return LightEngineResult.Failure(failures)
    }
}

sealed class LightEngineResult {
    data class Success(val searchResult: SearchResult) : LightEngineResult()
    data class Failure(val failures: List<FailureRecord>) : LightEngineResult()
}
