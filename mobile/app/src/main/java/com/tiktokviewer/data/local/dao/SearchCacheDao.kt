package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tiktokviewer.data.local.entity.SearchCacheEntity

@Dao
interface SearchCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cache: SearchCacheEntity)

    @Query("SELECT * FROM search_cache WHERE cacheKey = :key LIMIT 1")
    suspend fun get(key: String): SearchCacheEntity?

    @Query("UPDATE search_cache SET lastAccessed = :timestamp WHERE cacheKey = :key")
    suspend fun updateLastAccessed(key: String, timestamp: Long)

    @Query("DELETE FROM search_cache WHERE lastAccessed < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("SELECT COUNT(*) FROM search_cache")
    suspend fun count(): Int

    @Query("DELETE FROM search_cache WHERE id NOT IN (SELECT id FROM search_cache ORDER BY lastAccessed DESC LIMIT :keepCount)")
    suspend fun deleteAllExceptRecent(keepCount: Int)

    @Query("DELETE FROM search_cache")
    suspend fun deleteAll()
}
