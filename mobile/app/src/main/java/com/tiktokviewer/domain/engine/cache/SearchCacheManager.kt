package com.tiktokviewer.domain.engine.cache

import com.tiktokviewer.data.local.dao.SearchCacheDao
import com.tiktokviewer.data.local.entity.SearchCacheEntity
import com.tiktokviewer.domain.model.SearchResult
import com.google.gson.Gson

class SearchCacheManager(private val searchCacheDao: SearchCacheDao) {

    private val gson = Gson()

    suspend fun get(keyword: String): SearchResult? {
        val normalized = keyword.lowercase().trim()
        val entity = searchCacheDao.get(normalized) ?: return null

        val age = System.currentTimeMillis() - entity.createdAt
        if (age > entity.ttlSeconds * 1000L) {
            searchCacheDao.deleteByKey(normalized)
            return null
        }

        searchCacheDao.updateLastAccessed(normalized, System.currentTimeMillis())

        return try {
            gson.fromJson(entity.resultJson, SearchResult::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun put(keyword: String, result: SearchResult, ttlSeconds: Int = 1800) {
        val normalized = keyword.lowercase().trim()
        val entity = SearchCacheEntity(
            cacheKey = normalized,
            resultJson = gson.toJson(result),
            source = result.source.name,
            ttlSeconds = ttlSeconds,
            createdAt = System.currentTimeMillis(),
            lastAccessed = System.currentTimeMillis()
        )
        searchCacheDao.insert(entity)
    }

    suspend fun clearExpired() {
        val cutoff = System.currentTimeMillis() - (3600 * 1000L)
        searchCacheDao.deleteOlderThan(cutoff)
    }

    suspend fun clearAll() {
        searchCacheDao.deleteAll()
    }

    suspend fun enforceMaxEntries(maxEntries: Int = 500) {
        val count = searchCacheDao.count()
        if (count > maxEntries) {
            searchCacheDao.deleteAllExceptRecent(maxEntries)
        }
    }
}

private suspend fun SearchCacheDao.deleteByKey(key: String) {
    deleteOlderThan(System.currentTimeMillis() + 1)
}
