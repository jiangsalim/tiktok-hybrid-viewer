package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.SearchResult

interface CacheRepository {
    suspend fun cacheSearchResult(keyword: String, result: SearchResult)
    suspend fun getCachedSearch(keyword: String): SearchResult?
    suspend fun clearExpiredCache()
    suspend fun getCacheSizeBytes(): Long
    suspend fun clearAllCache()
}
