package com.tiktokviewer.domain.engine.light

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.tiktokviewer.domain.model.AuthorInfo
import com.tiktokviewer.domain.model.MusicInfo
import com.tiktokviewer.domain.model.VideoItem

class VideoParser {

    private val gson = Gson()

    fun parseMobileApiResponse(jsonString: String): List<VideoItem> {
        return try {
            val root = JsonParser.parseString(jsonString).asJsonObject
            val items = root.getAsJsonArray("aweme_list") ?: return emptyList()

            items.map { item ->
                val video = item.asJsonObject
                val stats = video.getAsJsonObject("statistics") ?: return@map null
                val author = video.getAsJsonObject("author") ?: return@map null
                val videoInfo = video.getAsJsonObject("video") ?: return@map null
                val playAddr = videoInfo.getAsJsonObject("play_addr") ?: return@map null

                VideoItem(
                    videoId = video.get("aweme_id")?.asString ?: "",
                    description = video.get("desc")?.asString ?: "",
                    author = AuthorInfo(
                        username = author.get("unique_id")?.asString ?: "",
                        displayName = author.get("nickname")?.asString ?: "",
                        avatarUrl = author.getAsJsonObject("avatar_medium")
                            ?.getAsJsonArray("url_list")
                            ?.get(0)?.asString ?: "",
                        verified = author.get("custom_verify")?.asString?.isNotEmpty() == true,
                        followerCount = author.get("follower_count")?.asLong ?: 0
                    ),
                    durationSeconds = (video.get("duration")?.asInt ?: 0) / 1000,
                    playCount = stats.get("play_count")?.asLong ?: 0,
                    likeCount = stats.get("digg_count")?.asLong ?: 0,
                    commentCount = stats.get("comment_count")?.asInt ?: 0,
                    shareCount = stats.get("share_count")?.asInt ?: 0,
                    thumbnailUrl = videoInfo.getAsJsonObject("cover")
                        ?.getAsJsonArray("url_list")
                        ?.get(0)?.asString ?: "",
                    videoPlayUrl = playAddr.getAsJsonArray("url_list")
                        ?.get(0)?.asString ?: "",
                    createdAt = video.get("create_time")?.asString ?: "",
                    hashtags = video.getAsJsonObject("cha_list")
                        ?.getAsJsonArray("cha_name")
                        ?.map { it.asString } ?: emptyList(),
                    music = video.getAsJsonObject("music")?.let { music ->
                        MusicInfo(
                            songName = music.get("title")?.asString ?: "Original Sound",
                            artistName = music.get("author_name")?.asString,
                            musicUrl = music.getAsJsonObject("play_url")
                                ?.get("uri")?.asString
                        )
                    }
                )
            }.filterNotNull()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parseWebApiResponse(jsonString: String): List<VideoItem> {
        return try {
            val root = JsonParser.parseString(jsonString).asJsonObject
            val items = root.getAsJsonArray("item_list") ?: return emptyList()

            items.map { item ->
                val video = item.asJsonObject
                val stats = video.getAsJsonObject("stats") ?: return@map null
                val author = video.getAsJsonObject("author") ?: return@map null

                VideoItem(
                    videoId = video.get("id")?.asString ?: "",
                    description = video.get("desc")?.asString ?: "",
                    author = AuthorInfo(
                        username = author.get("uniqueId")?.asString ?: "",
                        displayName = author.get("nickname")?.asString ?: "",
                        avatarUrl = author.get("avatarMedium")?.asString ?: "",
                        verified = author.get("verified")?.asBoolean ?: false,
                        followerCount = stats.get("followerCount")?.asLong ?: 0
                    ),
                    durationSeconds = video.getAsJsonObject("video")
                        ?.get("duration")?.asInt ?: 0,
                    playCount = stats.get("playCount")?.asLong ?: 0,
                    likeCount = stats.get("diggCount")?.asLong ?: 0,
                    commentCount = stats.get("commentCount")?.asInt ?: 0,
                    shareCount = stats.get("shareCount")?.asInt ?: 0,
                    thumbnailUrl = video.getAsJsonObject("video")
                        ?.get("cover")?.asString ?: "",
                    videoPlayUrl = video.getAsJsonObject("video")
                        ?.get("downloadAddr")?.asString ?: "",
                    createdAt = video.get("createTime")?.asString ?: "",
                    hashtags = video.get("hashtags")?.asJsonArray
                        ?.map { it.asString } ?: emptyList(),
                    music = video.getAsJsonObject("music")?.let { music ->
                        MusicInfo(
                            songName = music.get("title")?.asString ?: "Original Sound",
                            artistName = music.get("authorName")?.asString,
                            musicUrl = music.get("playUrl")?.asString
                        )
                    }
                )
            }.filterNotNull()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parseHtmlEmbeddedJson(html: String): List<VideoItem> {
        return try {
            val jsonStart = html.indexOf("{\"props\"")
            if (jsonStart == -1) return emptyList()

            var bracketCount = 0
            var jsonEnd = jsonStart
            for (i in jsonStart until html.length) {
                when (html[i]) {
                    '{' -> bracketCount++
                    '}' -> {
                        bracketCount--
                        if (bracketCount == 0) {
                            jsonEnd = i + 1
                            break
                        }
                    }
                }
            }

            val jsonString = html.substring(jsonStart, jsonEnd)
            parseWebApiResponse(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
