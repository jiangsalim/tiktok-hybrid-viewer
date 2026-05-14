package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.PeerTask
import com.tiktokviewer.domain.model.WorkerState

interface PeerRepository {
    suspend fun sendHeartbeat(state: WorkerState): Boolean
    suspend fun pollForTask(): PeerTask?
    suspend fun submitResult(taskId: String, result: SearchResult?, errorCode: String?): Boolean
    suspend fun requestPeerHelp(encryptedQuery: String, timeoutMs: Int): String?
    suspend fun checkTaskStatus(taskId: String): PeerTask?
}
