package com.tiktokviewer.domain.engine.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ThumbnailCacheManager(context: Context) {

    private val cacheDir = File(context.cacheDir, "thumbnails").apply {
        if (!exists()) mkdirs()
    }

    suspend fun get(url: String): Bitmap? = withContext(Dispatchers.IO) {
        val file = getFile(url)
        if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    suspend fun put(url: String, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val file = getFile(url)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        }
    }

    fun getFile(url: String): File {
        val key = url.hashCode().toString()
        return File(cacheDir, "thumb_$key.jpg")
    }

    suspend fun getSizeBytes(): Long = withContext(Dispatchers.IO) {
        cacheDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    suspend fun clear() = withContext(Dispatchers.IO) {
        cacheDir.deleteRecursively()
        cacheDir.mkdirs()
    }

    suspend fun enforceMaxSize(maxBytes: Long) = withContext(Dispatchers.IO) {
        val currentSize = getSizeBytes()
        if (currentSize > maxBytes) {
            val files = cacheDir.listFiles()?.sortedBy { it.lastModified() } ?: return@withContext
            var freed = 0L
            val target = maxBytes * 8 / 10
            for (file in files) {
                if (currentSize - freed <= target) break
                freed += file.length()
                file.delete()
            }
        }
    }
}
