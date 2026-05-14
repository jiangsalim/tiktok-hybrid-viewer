package com.tiktokviewer.domain.engine

import android.content.Context
import com.tiktokviewer.data.local.prefs.PreferencesManager
import com.tiktokviewer.data.remote.ApiService
import com.tiktokviewer.data.remote.dto.SignatureCheckRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

class SignatureUpdateEngine(
    private val context: Context,
    private val apiService: ApiService,
    private val prefs: PreferencesManager
) {
    companion object {
        private const val KEY_ACTIVE_VERSION = "active_signer_version"
        private const val KEY_STAGED_VERSION = "staged_signer_version"
        private const val KEY_STAGED_PATH = "staged_signer_path"
        private const val DEFAULT_VERSION = "builtin"
    }

    suspend fun checkForUpdate(deviceId: String): UpdateResult = withContext(Dispatchers.IO) {
        try {
            val currentVersion = getActiveVersion()
            val response = apiService.signatureCheck(
                deviceId = deviceId,
                request = SignatureCheckRequest(deviceId, currentVersion, null)
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.updateAvailable == true && body.update != null) {
                    return@withContext UpdateResult.Available(
                        version = body.latestSignatureVersion ?: "",
                        url = body.update.fileUrl,
                        checksum = body.update.checksumSha256,
                        urgency = body.update.urgency
                    )
                }
            }
            UpdateResult.UpToDate
        } catch (e: Exception) {
            UpdateResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun downloadAndStage(url: String, expectedChecksum: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val tempFile = File(context.cacheDir, "signer_update.so")
            val response = okhttp3.OkHttpClient().newCall(
                okhttp3.Request.Builder().url(url).build()
            ).execute()

            if (!response.isSuccessful) return@withContext false

            response.body?.bytes()?.let { bytes ->
                val actualChecksum = MessageDigest.getInstance("SHA-256")
                    .digest(bytes)
                    .joinToString("") { "%02x".format(it) }

                if (actualChecksum != expectedChecksum) {
                    return@withContext false
                }

                tempFile.writeBytes(bytes)
                prefs.putString(KEY_STAGED_PATH, tempFile.absolutePath)
                prefs.putString(KEY_STAGED_VERSION, "updated")
                return@withContext true
            }
            false
        } catch (e: Exception) {
            false
        }
    }

    fun applyStagedUpdate(): Boolean {
        val stagedPath = prefs.getString(KEY_STAGED_PATH)
        if (stagedPath.isEmpty()) return false

        val stagedFile = File(stagedPath)
        if (!stagedFile.exists()) {
            prefs.remove(KEY_STAGED_PATH)
            prefs.remove(KEY_STAGED_VERSION)
            return false
        }

        val activePath = File(context.filesDir, "signer_active.so")
        stagedFile.copyTo(activePath, overwrite = true)
        stagedFile.delete()

        prefs.putString(KEY_ACTIVE_VERSION, prefs.getString(KEY_STAGED_VERSION, "updated"))
        prefs.remove(KEY_STAGED_PATH)
        prefs.remove(KEY_STAGED_VERSION)
        return true
    }

    fun rollbackToBuiltin() {
        prefs.putString(KEY_ACTIVE_VERSION, DEFAULT_VERSION)
        val activePath = File(context.filesDir, "signer_active.so")
        activePath.delete()
    }

    fun getActiveVersion(): String {
        return prefs.getString(KEY_ACTIVE_VERSION, DEFAULT_VERSION)
    }
}

sealed class UpdateResult {
    object UpToDate : UpdateResult()
    data class Available(
        val version: String,
        val url: String,
        val checksum: String,
        val urgency: String
    ) : UpdateResult()
    data class Error(val message: String) : UpdateResult()
}
