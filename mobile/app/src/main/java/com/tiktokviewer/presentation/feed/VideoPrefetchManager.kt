package com.tiktokviewer.presentation.feed

import com.tiktokviewer.domain.model.VideoItem

class VideoPrefetchManager {

    private var loadedVideos = mutableListOf<VideoItem>()
    private var currentIndex = 0
    private var onNeedMoreVideos: (() -> Unit)? = null

    fun setOnNeedMoreVideos(callback: () -> Unit) {
        onNeedMoreVideos = callback
    }

    fun setVideos(videos: List<VideoItem>) {
        loadedVideos.clear()
        loadedVideos.addAll(videos)
        currentIndex = 0
    }

    fun appendVideos(videos: List<VideoItem>) {
        loadedVideos.addAll(videos)
        checkPrefetch()
    }

    fun moveToIndex(index: Int): VideoItem? {
        if (index < 0 || index >= loadedVideos.size) return null
        currentIndex = index
        checkPrefetch()
        return loadedVideos[index]
    }

    fun getCurrentVideo(): VideoItem? {
        return if (loadedVideos.isNotEmpty() && currentIndex < loadedVideos.size) {
            loadedVideos[currentIndex]
        } else null
    }

    fun getNextVideo(): VideoItem? {
        val nextIndex = currentIndex + 1
        return if (nextIndex < loadedVideos.size) {
            loadedVideos[nextIndex]
        } else null
    }

    fun getPreviousVideo(): VideoItem? {
        val prevIndex = currentIndex - 1
        return if (prevIndex >= 0) {
            loadedVideos[prevIndex]
        } else null
    }

    fun videosRemaining(): Int {
        return (loadedVideos.size - currentIndex - 1).coerceAtLeast(0)
    }

    fun hasMore(): Boolean = true

    private fun checkPrefetch() {
        if (videosRemaining() <= 3) {
            onNeedMoreVideos?.invoke()
        }
    }

    fun getCurrentIndex(): Int = currentIndex

    fun getTotalLoaded(): Int = loadedVideos.size
}
