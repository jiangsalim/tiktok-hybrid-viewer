package com.tiktokviewer.domain.engine

import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.math.pow

class SmartRetryPolicy {

    data class RetryConfig(
        val maxRetries: Int,
        val baseDelayMs: Long,
        val maxDelayMs: Long = 30000L,
        val backoffMultiplier: Double = 2.0
    )

    suspend fun <T> retry(
        config: RetryConfig = RetryConfig(maxRetries = 3, baseDelayMs = 1000L),
        operation: suspend () -> T
    ): Result<T> {
        var lastException: Exception? = null

        for (attempt in 0..config.maxRetries) {
            try {
                if (attempt > 0) {
                    val delayMs = min(
                        config.baseDelayMs * config.backoffMultiplier.pow(attempt - 1).toLong(),
                        config.maxDelayMs
                    )
                    delay(delayMs)
                }
                return Result.success(operation())
            } catch (e: Exception) {
                lastException = e
                if (!isRetryable(e) || attempt == config.maxRetries) {
                    return Result.failure(e)
                }
            }
        }

        return Result.failure(lastException ?: Exception("Retry exhausted"))
    }

    private fun isRetryable(exception: Exception): Boolean {
        val message = exception.message?.lowercase() ?: ""
        return when {
            message.contains("timeout") -> true
            message.contains("network") -> true
            message.contains("connection") -> true
            message.contains("dns") -> true
            message.contains("429") -> true
            message.contains("503") -> true
            message.contains("unreachable") -> true
            message.contains("econnrefused") -> true
            message.contains("econnreset") -> true
            else -> false
        }
    }
}
