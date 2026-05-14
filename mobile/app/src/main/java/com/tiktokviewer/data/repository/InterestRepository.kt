package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.InterestProfile
import com.tiktokviewer.domain.model.SignalEvent

interface InterestRepository {
    suspend fun captureSignal(event: SignalEvent)
    suspend fun getProfile(): InterestProfile?
    suspend fun updateProfile(profile: InterestProfile)
    suspend fun decayProfile()
    suspend fun clearProfile()
    suspend fun getRecentSignals(since: Long): List<SignalEvent>
}
