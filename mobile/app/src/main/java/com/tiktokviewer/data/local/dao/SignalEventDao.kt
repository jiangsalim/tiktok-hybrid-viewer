package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tiktokviewer.data.local.entity.SignalEventEntity

@Dao
interface SignalEventDao {

    @Insert
    suspend fun insert(event: SignalEventEntity): Long

    @Query("SELECT * FROM signal_events WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getBySession(sessionId: String): List<SignalEventEntity>

    @Query("SELECT * FROM signal_events WHERE signalType = :type AND timestamp > :since ORDER BY timestamp DESC")
    suspend fun getByTypeSince(type: String, since: Long): List<SignalEventEntity>

    @Query("SELECT * FROM signal_events WHERE videoId = :videoId ORDER BY timestamp DESC")
    suspend fun getByVideoId(videoId: String): List<SignalEventEntity>

    @Query("DELETE FROM signal_events WHERE timestamp < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("SELECT COUNT(*) FROM signal_events WHERE timestamp > :since")
    suspend fun countSince(since: Long): Int

    @Query("DELETE FROM signal_events")
    suspend fun deleteAll()
}
