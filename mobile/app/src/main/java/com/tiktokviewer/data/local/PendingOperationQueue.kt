package com.tiktokviewer.data.local

import com.tiktokviewer.data.local.dao.PendingOperationDao
import com.tiktokviewer.data.local.entity.PendingOperationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PendingOperationQueue(private val dao: PendingOperationDao) {

    suspend fun enqueue(operationType: String, payloadJson: String) {
        dao.insert(
            PendingOperationEntity(
                operationType = operationType,
                payloadJson = payloadJson,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getNextBatch(): List<PendingOperationEntity> {
        return dao.getPendingOperations(System.currentTimeMillis())
    }

    suspend fun markRetry(id: Long) {
        val backoff = 5000L
        dao.updateRetry(id, System.currentTimeMillis() + backoff)
    }

    suspend fun remove(id: Long) {
        dao.delete(id)
    }

    suspend fun processAll(
        processor: suspend (PendingOperationEntity) -> Boolean
    ) = withContext(Dispatchers.IO) {
        val operations = getNextBatch()
        for (op in operations) {
            val success = try {
                processor(op)
            } catch (e: Exception) {
                false
            }
            if (success) {
                remove(op.id)
            } else if (op.retryCount >= op.maxRetries) {
                remove(op.id)
            } else {
                markRetry(op.id)
            }
        }
    }
}
