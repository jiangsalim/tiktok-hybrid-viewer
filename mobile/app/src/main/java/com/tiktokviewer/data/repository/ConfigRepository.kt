package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.RemoteConfig

interface ConfigRepository {
    suspend fun fetchRemoteConfig(): RemoteConfig?
    suspend fun getCachedConfig(): RemoteConfig?
}
