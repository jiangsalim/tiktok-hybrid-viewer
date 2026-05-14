package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tiktokviewer.data.local.entity.PendingOperationEntity

@Dao
interface PendingOperationDao {

    @Insert
    suspend fun insert(operation: PendingOperationEntity)

    @Query("SELECT * FROM pending_operations WHERE nextRetryAt IS NULL OR nextRetryAt <= :now ORDER BY createdAt ASC")
    suspend fun getPendingOperations(now: Long): List<PendingOperationEntity>

    @Query("UPDATE pending_operations SET retryCount = retryCount + 1, nextRetryAt = :nextRetry WHERE id = :id")
    suspend fun updateRetry(id: Long, nextRetry: Long)

    @Query("DELETE FROM pending_operations WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT COUNT(*) FROM pending_operations")
    suspend fun count(): Int
}
