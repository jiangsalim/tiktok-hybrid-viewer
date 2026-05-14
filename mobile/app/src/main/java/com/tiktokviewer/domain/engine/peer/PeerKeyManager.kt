package com.tiktokviewer.domain.engine.peer

import com.tiktokviewer.data.local.prefs.PreferencesManager
import com.tiktokviewer.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import javax.crypto.spec.SecretKeySpec

class PeerKeyManager(
    private val prefs: PreferencesManager,
    private val apiService: ApiService
) {
    companion object {
        private const val KEY_NETWORK_KEY = "peer_network_key"
        private const val KEY_KEY_EPOCH = "peer_key_epoch"
    }

    suspend fun getNetworkKey(): ByteArray = withContext(Dispatchers.IO) {
        val cached = loadCachedKey()
        if (cached != null && isCurrentEpoch()) {
            return@withContext cached
        }
        val newKey = fetchKeyFromServer()
        if (newKey != null) {
            saveKey(newKey)
            newKey
        } else {
            cached ?: generateFallbackKey()
        }
    }

    private suspend fun fetchKeyFromServer(): ByteArray? {
        return try {
            val response = apiService.getConfig(
                deviceId = "peer",
                com.tiktokviewer.data.remote.dto.ConfigRequest("peer", "1.0.0")
            )
            if (response.isSuccessful) {
                val epoch = System.currentTimeMillis() / (7 * 24 * 60 * 60 * 1000L)
                val raw = "tiktok-viewer-peer-key-v1-$epoch"
                MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun loadCachedKey(): ByteArray? {
        val encoded = prefs.getString(KEY_NETWORK_KEY)
        if (encoded.isEmpty()) return null
        return try {
            android.util.Base64.decode(encoded, android.util.Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    private fun saveKey(key: ByteArray) {
        val encoded = android.util.Base64.encodeToString(key, android.util.Base64.NO_WRAP)
        prefs.putString(KEY_NETWORK_KEY, encoded)
        prefs.putLong(KEY_KEY_EPOCH, System.currentTimeMillis() / (7 * 24 * 60 * 60 * 1000L))
    }

    private fun isCurrentEpoch(): Boolean {
        val currentEpoch = System.currentTimeMillis() / (7 * 24 * 60 * 60 * 1000L)
        val storedEpoch = prefs.getLong(KEY_KEY_EPOCH)
        return currentEpoch == storedEpoch
    }

    private fun generateFallbackKey(): ByteArray {
        val raw = "tiktok-viewer-fallback-key"
        return MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
    }
}
