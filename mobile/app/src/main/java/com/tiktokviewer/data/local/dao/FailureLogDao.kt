package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tiktokviewer.data.local.entity.FailureLogEntity

@Dao
interface FailureLogDao {

    @Insert
    suspend fun insert(failure: FailureLogEntity)

    @Query("SELECT * FROM failure_log WHERE searchId = :searchId ORDER BY timestamp ASC")
    suspend fun getBySearchId(searchId: String): List<FailureLogEntity>

    @Query("SELECT COUNT(*) FROM failure_log WHERE engine = :engine AND timestamp > :since")
    suspend fun countByEngineSince(engine: String, since: Long): Int

    @Query("DELETE FROM failure_log WHERE timestamp < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("DELETE FROM failure_log")
    suspend fun deleteAll()
}
