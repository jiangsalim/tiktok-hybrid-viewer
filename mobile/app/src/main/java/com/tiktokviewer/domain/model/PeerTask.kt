package com.tiktokviewer.domain.model

data class PeerTask(
    val taskId: String,
    val requesterId: String,
    val workerId: String? = null,
    val encryptedQuery: String,
    val status: PeerTaskStatus = PeerTaskStatus.PENDING,
    val result: SearchResult? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val claimedAt: Long? = null,
    val completedAt: Long? = null,
    val ttlSeconds: Int = 30
)
