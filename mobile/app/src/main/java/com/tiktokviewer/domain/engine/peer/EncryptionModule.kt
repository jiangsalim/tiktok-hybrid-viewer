package com.tiktokviewer.domain.engine.peer

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionModule {

    companion object {
        private const val GCM_TAG_LENGTH = 128
        private const val GCM_IV_LENGTH = 12
    }

    fun encrypt(plaintext: String, keyBytes: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key: SecretKey = SecretKeySpec(keyBytes, "AES")
        val iv = ByteArray(GCM_IV_LENGTH)
        java.security.SecureRandom().nextBytes(iv)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, spec)
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        val combined = ByteArray(iv.size + ciphertext.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(ciphertext, 0, combined, iv.size, ciphertext.size)

        return "AES256:" + Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decrypt(encryptedData: String, keyBytes: ByteArray): String? {
        return try {
            if (!encryptedData.startsWith("AES256:")) return null
            val combined = Base64.decode(encryptedData.substring(6), Base64.NO_WRAP)
            val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
            val ciphertext = combined.copyOfRange(GCM_IV_LENGTH, combined.size)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val key: SecretKey = SecretKeySpec(keyBytes, "AES")
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, key, spec)
            String(cipher.doFinal(ciphertext), Charsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }
}
