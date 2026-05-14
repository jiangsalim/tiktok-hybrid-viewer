package com.tiktokviewer.data.repository

interface SignatureRepository {
    suspend fun checkForUpdate(currentVersion: String): SignatureUpdateInfo?
    suspend fun downloadUpdate(url: String, checksum: String): Boolean
    suspend fun proxySign(url: String, method: String): ProxySignResult?
}

data class SignatureUpdateInfo(
    val version: String,
    val fileUrl: String,
    val checksumSha256: String,
    val urgency: String
)

data class ProxySignResult(
    val signedUrl: String,
    val signedHeaders: Map<String, String>,
    val expiresAt: String
)
