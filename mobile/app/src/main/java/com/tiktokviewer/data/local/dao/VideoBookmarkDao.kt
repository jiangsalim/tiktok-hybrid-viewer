package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tiktokviewer.data.local.entity.VideoBookmarkEntity

@Dao
interface VideoBookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: VideoBookmarkEntity)

    @Query("SELECT * FROM video_bookmarks WHERE videoId = :videoId LIMIT 1")
    suspend fun getByVideoId(videoId: String): VideoBookmarkEntity?

    @Query("SELECT * FROM video_bookmarks ORDER BY createdAt DESC")
    suspend fun getAll(): List<VideoBookmarkEntity>

    @Query("DELETE FROM video_bookmarks WHERE videoId = :videoId")
    suspend fun delete(videoId: String)

    @Query("UPDATE video_bookmarks SET videoPath = :path, isDownloaded = 1 WHERE videoId = :videoId")
    suspend fun markDownloaded(videoId: String, path: String)

    @Query("SELECT COUNT(*) FROM video_bookmarks")
    suspend fun count(): Int
}
