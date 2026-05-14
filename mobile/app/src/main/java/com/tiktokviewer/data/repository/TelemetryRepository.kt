package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.FailureRecord

interface TelemetryRepository {
    suspend fun reportFailure(searchId: String, keyword: String?, attempts: List<FailureRecord>)
    suspend fun reportSuccess(engine: String, phase: String?, keyword: String?, latencyMs: Long, resultsCount: Int?)
}
