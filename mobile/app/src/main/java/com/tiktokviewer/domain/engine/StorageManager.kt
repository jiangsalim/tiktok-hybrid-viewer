package com.tiktokviewer.domain.engine

import android.content.Context
import com.tiktokviewer.domain.engine.cache.ThumbnailCacheManager
import com.tiktokviewer.domain.engine.cache.VideoCacheManager
import com.tiktokviewer.data.local.dao.SearchCacheDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class StorageManager(
    private val context: Context,
    private val thumbnailCacheManager: ThumbnailCacheManager,
    private val videoCacheManager: VideoCacheManager,
    private val searchCacheDao: SearchCacheDao
) {
    data class StorageBreakdown(
        val videoCacheBytes: Long = 0,
        val thumbnailCacheBytes: Long = 0,
        val searchCacheEntries: Int = 0,
        val totalBytes: Long = 0
    )

    companion object {
        const val MAX_VIDEO_CACHE_BYTES = 80L * 1024 * 1024
        const val MAX_THUMBNAIL_CACHE_BYTES = 20L * 1024 * 1024
        const val MAX_SEARCH_CACHE_ENTRIES = 500
    }

    suspend fun getBreakdown(): StorageBreakdown = withContext(Dispatchers.IO) {
        val videoBytes = videoCacheManager.getCacheSizeBytes()
        val thumbBytes = thumbnailCacheManager.getSizeBytes()
        val searchEntries = searchCacheDao.count()
        StorageBreakdown(
            videoCacheBytes = videoBytes,
            thumbnailCacheBytes = thumbBytes,
            searchCacheEntries = searchEntries,
            totalBytes = videoBytes + thumbBytes
        )
    }

    suspend fun enforceLimits() = withContext(Dispatchers.IO) {
        thumbnailCacheManager.enforceMaxSize(MAX_THUMBNAIL_CACHE_BYTES)

        val videoBytes = videoCacheManager.getCacheSizeBytes()
        if (videoBytes > MAX_VIDEO_CACHE_BYTES) {
            videoCacheManager.clearCache()
        }

        val searchEntries = searchCacheDao.count()
        if (searchEntries > MAX_SEARCH_CACHE_ENTRIES) {
            searchCacheDao.deleteAllExceptRecent(MAX_SEARCH_CACHE_ENTRIES)
        }
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        videoCacheManager.clearCache()
        thumbnailCacheManager.clear()
        searchCacheDao.deleteAll()
    }

    fun getFreeSpaceBytes(): Long {
        val cacheDir = context.cacheDir
        val stat = android.os.StatFs(cacheDir.absolutePath)
        return stat.availableBlocksLong * stat.blockSizeLong
    }

    fun isLowStorage(): Boolean {
        return getFreeSpaceBytes() < 100L * 1024 * 1024
    }
}
