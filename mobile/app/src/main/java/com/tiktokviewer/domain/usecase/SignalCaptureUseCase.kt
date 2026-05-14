package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.local.dao.SignalEventDao
import com.tiktokviewer.data.local.entity.SignalEventEntity
import com.tiktokviewer.domain.model.SignalType
import com.google.gson.Gson

class SignalCaptureUseCase(private val signalEventDao: SignalEventDao) {

    private val gson = Gson()
    private var currentSessionId: String = generateSessionId()

    suspend fun capture(
        type: SignalType,
        deviceId: String,
        videoId: String? = null,
        keyword: String? = null,
        authorUsername: String? = null,
        hashtags: List<String>? = null,
        musicId: String? = null,
        watchPercent: Float? = null,
        scrollSpeed: Float? = null,
        extraJson: String? = null
    ) {
        val entity = SignalEventEntity(
            sessionId = currentSessionId,
            signalType = type.name,
            videoId = videoId,
            keyword = keyword,
            authorUsername = authorUsername,
            hashtagsJson = hashtags?.let { gson.toJson(it) },
            musicId = musicId,
            watchPercent = watchPercent,
            scrollSpeed = scrollSpeed,
            extraJson = extraJson,
            timestamp = System.currentTimeMillis(),
            deviceId = deviceId
        )
        signalEventDao.insert(entity)
    }

    fun newSession() {
        currentSessionId = generateSessionId()
    }

    fun getCurrentSessionId(): String = currentSessionId

    private fun generateSessionId(): String {
        return "session_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
}
