package com.tiktokviewer.domain.engine.light

class SignatureGenerator {

    companion object {
        init {
            try {
                System.loadLibrary("signer_bridge")
            } catch (e: UnsatisfiedLinkError) {
                e.printStackTrace()
            }
        }
    }

    private external fun nativeSignMobileApi(
        url: String,
        deviceId: String,
        timestamp: Long
    ): String

    private external fun nativeSignWebApi(
        url: String,
        userAgent: String,
        timestamp: Long
    ): String

    fun signMobileApi(url: String, deviceId: String): MobileSignedHeaders {
        val timestamp = System.currentTimeMillis() / 1000
        return try {
            val raw = nativeSignMobileApi(url, deviceId, timestamp)
            val parts = raw.split("|")
            MobileSignedHeaders(
                xArgus = parts.getOrElse(0) { "" },
                xGorgon = parts.getOrElse(1) { "" },
                xLadon = parts.getOrElse(2) { "" },
                xKhronos = parts.getOrElse(3) { timestamp.toString() }
            )
        } catch (e: Exception) {
            MobileSignedHeaders(
                xArgus = "",
                xGorgon = "",
                xLadon = "",
                xKhronos = timestamp.toString()
            )
        }
    }

    fun signWebApi(url: String, userAgent: String): WebSignedHeaders {
        val timestamp = System.currentTimeMillis() / 1000
        return try {
            val raw = nativeSignWebApi(url, userAgent, timestamp)
            val parts = raw.split("|")
            WebSignedHeaders(
                xBogus = parts.getOrElse(0) { "" },
                xGnarly = parts.getOrElse(1) { "" },
                timestamp = parts.getOrElse(2) { timestamp.toString() }.toLongOrNull() ?: timestamp
            )
        } catch (e: Exception) {
            WebSignedHeaders(
                xBogus = "",
                xGnarly = "",
                timestamp = timestamp
            )
        }
    }
}

data class MobileSignedHeaders(
    val xArgus: String,
    val xGorgon: String,
    val xLadon: String,
    val xKhronos: String
)

data class WebSignedHeaders(
    val xBogus: String,
    val xGnarly: String,
    val timestamp: Long
)
