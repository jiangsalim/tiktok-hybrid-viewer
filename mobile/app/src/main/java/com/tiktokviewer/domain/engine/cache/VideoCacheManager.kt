package com.tiktokviewer.domain.engine.cache

import android.content.Context
import java.io.File

class VideoCacheManager(context: Context) {

    private val cacheDir = File(context.cacheDir, "videos").apply {
        if (!exists()) mkdirs()
    }
    private val bookmarkedDir = File(context.filesDir, "saved_videos").apply {
        if (!exists()) mkdirs()
    }

    fun getCachedFile(videoId: String): File? {
        val file = File(cacheDir, "${videoId}.mp4")
        return if (file.exists()) file else null
    }

    fun getBookmarkedFile(videoId: String): File? {
        val file = File(bookmarkedDir, "${videoId}.mp4")
        return if (file.exists()) file else null
    }

    fun getVideoFile(videoId: String): File {
        return File(cacheDir, "${videoId}.mp4")
    }

    fun moveToBookmarks(videoId: String): File? {
        val source = File(cacheDir, "${videoId}.mp4")
        if (!source.exists()) return null
        val dest = File(bookmarkedDir, "${videoId}.mp4")
        source.copyTo(dest, overwrite = true)
        source.delete()
        return dest
    }

    fun removeBookmark(videoId: String) {
        File(bookmarkedDir, "${videoId}.mp4").delete()
    }

    fun getCacheSizeBytes(): Long {
        return cacheDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    fun clearAll() {
        clearCache()
        bookmarkedDir.listFiles()?.forEach { it.delete() }
    }

    fun isBookmarkDownloaded(videoId: String): Boolean {
        return File(bookmarkedDir, "${videoId}.mp4").exists()
    }
}
