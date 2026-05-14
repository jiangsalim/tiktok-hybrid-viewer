package com.tiktokviewer.domain.engine.peer

import com.tiktokviewer.data.remote.ApiService
import com.tiktokviewer.data.remote.dto.PeerRequestRequest
import com.tiktokviewer.data.remote.dto.PeerStatusRequest
import kotlinx.coroutines.delay

class PeerRequester(
    private val apiService: ApiService,
    private val encryptionModule: EncryptionModule,
    private val peerKeyManager: PeerKeyManager
) {
    suspend fun askNetwork(
        keyword: String,
        deviceId: String,
        timeoutMs: Int = 15000
    ): PeerRequestResult {
        return try {
            val key = peerKeyManager.getNetworkKey()
            val encryptedQuery = encryptionModule.encrypt(keyword, key)

            val requestResponse = apiService.peerRequest(
                deviceId = deviceId,
                request = PeerRequestRequest(
                    requesterId = deviceId,
                    encryptedQuery = encryptedQuery,
                    timeoutMs = timeoutMs
                )
            )

            if (!requestResponse.isSuccessful) {
                return PeerRequestResult.Failure("peer_request_failed")
            }

            val body = requestResponse.body()
            if (body?.status == "no_peers_available") {
                return PeerRequestResult.Failure("no_peers_available")
            }

            val taskId = body?.taskId ?: return PeerRequestResult.Failure("no_task_id")

            val startTime = System.currentTimeMillis()
            val pollInterval = 1000L

            while (System.currentTimeMillis() - startTime < timeoutMs) {
                delay(pollInterval)

                val statusResponse = apiService.peerStatus(
                    deviceId = deviceId,
                    request = PeerStatusRequest(deviceId, taskId)
                )

                if (statusResponse.isSuccessful) {
                    val statusBody = statusResponse.body()
                    when (statusBody?.status) {
                        "completed" -> {
                            val videos = statusBody.result?.videos?.map { it.toDomain() } ?: emptyList()
                            return PeerRequestResult.Success(videos)
                        }
                        "failed", "expired" -> {
                            return PeerRequestResult.Failure(
                                statusBody.reason ?: "peer_task_${statusBody.status}"
                            )
                        }
                    }
                }
            }
            PeerRequestResult.Failure("peer_request_timeout")
        } catch (e: Exception) {
            PeerRequestResult.Failure("peer_request_exception: ${e.message}")
        }
    }
}

sealed class PeerRequestResult {
    data class Success(val videos: List<com.tiktokviewer.domain.model.VideoItem>) : PeerRequestResult()
    data class Failure(val errorCode: String) : PeerRequestResult()
}

private fun com.tiktokviewer.data.remote.dto.VideoItemDto.toDomain(): com.tiktokviewer.domain.model.VideoItem {
    return com.tiktokviewer.domain.model.VideoItem(
        videoId = videoId,
        description = description ?: "",
        author = com.tiktokviewer.domain.model.AuthorInfo(
            username = author?.username ?: "",
            displayName = author?.displayName ?: "",
            avatarUrl = author?.avatarUrl ?: "",
            verified = author?.verified ?: false,
            followerCount = author?.followerCount ?: 0
        ),
        durationSeconds = durationSeconds ?: 0,
        playCount = playCount ?: 0,
        likeCount = likeCount ?: 0,
        commentCount = commentCount ?: 0,
        shareCount = shareCount ?: 0,
        thumbnailUrl = thumbnailUrl ?: "",
        videoPlayUrl = videoPlayUrl ?: "",
        createdAt = createdAt ?: "",
        hashtags = hashtags ?: emptyList(),
        music = music?.let {
            com.tiktokviewer.domain.model.MusicInfo(
                songName = it.songName ?: "Original Sound",
                artistName = it.artistName,
                musicUrl = it.musicUrl
            )
        }
    )
}
