package com.tiktokviewer.domain.usecase

import android.webkit.WebView
import com.tiktokviewer.domain.engine.cache.SearchCacheManager
import com.tiktokviewer.domain.engine.heavy.HeavyEngine
import com.tiktokviewer.domain.engine.heavy.HeavySearchResult
import com.tiktokviewer.domain.engine.light.LightEngine
import com.tiktokviewer.domain.engine.light.LightEngineResult
import com.tiktokviewer.domain.model.FailureRecord
import com.tiktokviewer.domain.model.SearchResult

class SearchOrchestrator(
    private val lightEngine: LightEngine,
    private val heavyEngine: HeavyEngine,
    private val searchCacheManager: SearchCacheManager
) {
    suspend fun search(
        keyword: String,
        deviceId: String,
        webView: WebView? = null
    ): OrchestratorResult {
        val searchId = "search_${System.currentTimeMillis()}"
        val allFailures = mutableListOf<FailureRecord>()

        // Step 1: Check cache
        val cached = searchCacheManager.get(keyword)
        if (cached != null) {
            val age = System.currentTimeMillis() - cached.timestamp
            if (age < (cachedTimestampToTtl() * 500L)) {
                return OrchestratorResult.Success(cached)
            }
        }

        // Step 2: Light Engine
        val lightStart = System.currentTimeMillis()
        when (val lightResult = lightEngine.search(keyword, deviceId)) {
            is LightEngineResult.Success -> {
                val result = lightResult.searchResult
                searchCacheManager.put(keyword, result)
                searchCacheManager.enforceMaxEntries()
                return OrchestratorResult.Success(result)
            }
            is LightEngineResult.Failure -> {
                allFailures.addAll(lightResult.failures)
            }
        }

        // Step 3: Heavy Engine (on-device WebView)
        if (webView != null) {
            val heavyStart = System.currentTimeMillis()
            when (val heavyResult = heavyEngine.search(keyword, deviceId, webView)) {
                is HeavySearchResult.Success -> {
                    val result = heavyResult.searchResult
                    searchCacheManager.put(keyword, result)
                    return OrchestratorResult.Success(result)
                }
                is HeavySearchResult.Failure -> {
                    allFailures.addAll(heavyResult.failures)
                }
            }
        }

        // Step 4: All local attempts failed
        return OrchestratorResult.NeedsRemoteFallback(
            searchId = searchId,
            keyword = keyword,
            failures = allFailures
        )
    }

    private fun cachedTimestampToTtl(): Long {
        return 1800L
    }
}

sealed class OrchestratorResult {
    data class Success(val searchResult: SearchResult) : OrchestratorResult()
    data class NeedsRemoteFallback(
        val searchId: String,
        val keyword: String,
        val failures: List<FailureRecord>
    ) : OrchestratorResult()
}
