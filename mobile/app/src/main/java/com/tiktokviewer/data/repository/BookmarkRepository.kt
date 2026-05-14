package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.VideoItem

interface BookmarkRepository {
    suspend fun bookmark(video: VideoItem)
    suspend fun removeBookmark(videoId: String)
    suspend fun isBookmarked(videoId: String): Boolean
    suspend fun getAllBookmarks(): List<VideoItem>
    suspend fun downloadVideo(videoId: String, url: String): Boolean
    suspend fun getDownloadedPath(videoId: String): String?
}
