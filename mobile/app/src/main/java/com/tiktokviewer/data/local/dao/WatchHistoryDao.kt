package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tiktokviewer.data.local.entity.WatchHistoryEntity

@Dao
interface WatchHistoryDao {

    @Insert
    suspend fun insert(entry: WatchHistoryEntity)

    @Query("SELECT * FROM watch_history WHERE videoId = :videoId ORDER BY watchedAt DESC LIMIT 1")
    suspend fun getLatestByVideoId(videoId: String): WatchHistoryEntity?

    @Query("SELECT * FROM watch_history ORDER BY watchedAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 100): List<WatchHistoryEntity>

    @Query("DELETE FROM watch_history WHERE watchedAt < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("SELECT COUNT(*) FROM watch_history WHERE watchedAt > :since")
    suspend fun countSince(since: Long): Int

    @Query("DELETE FROM watch_history")
    suspend fun deleteAll()
}
