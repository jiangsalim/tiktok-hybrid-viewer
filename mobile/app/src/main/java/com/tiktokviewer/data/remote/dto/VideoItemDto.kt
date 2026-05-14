package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoItemDto(
    @SerializedName("video_id") val videoId: String,
    @SerializedName("description") val description: String?,
    @SerializedName("author") val author: AuthorDto?,
    @SerializedName("duration_seconds") val durationSeconds: Int?,
    @SerializedName("play_count") val playCount: Long?,
    @SerializedName("like_count") val likeCount: Long?,
    @SerializedName("comment_count") val commentCount: Int?,
    @SerializedName("share_count") val shareCount: Int?,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("video_play_url") val videoPlayUrl: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("hashtags") val hashtags: List<String>?,
    @SerializedName("music") val music: MusicDto?
)

data class AuthorDto(
    @SerializedName("username") val username: String,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("verified") val verified: Boolean?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("follower_count") val followerCount: Long?
)

data class MusicDto(
    @SerializedName("song_name") val songName: String?,
    @SerializedName("artist_name") val artistName: String?,
    @SerializedName("music_url") val musicUrl: String?
)
