package com.tiktokviewer.domain.usecase

import com.tiktokviewer.data.local.dao.SignalEventDao
import com.tiktokviewer.data.local.prefs.InterestProfileStore
import com.tiktokviewer.domain.model.CreatorAffinity
import com.tiktokviewer.domain.model.InterestProfile
import com.tiktokviewer.domain.model.TimePatterns

class InterestDecayUseCase(
    private val interestProfileStore: InterestProfileStore,
    private val signalEventDao: SignalEventDao
) {
    companion object {
        private const val DECAY_FACTOR = 0.85f
        private const val DECAY_INTERVAL_MS = 7 * 24 * 60 * 60 * 1000L
        private const val MIN_THRESHOLD = 0.05f
    }

    suspend fun decayIfNeeded() {
        val profile = interestProfileStore.load() ?: return

        val timeSinceLastDecay = System.currentTimeMillis() - profile.lastDecayRun
        val decayCycles = timeSinceLastDecay / DECAY_INTERVAL_MS

        if (decayCycles < 1) return

        var decayMultiplier = 1f
        repeat(decayCycles.toInt()) {
            decayMultiplier *= DECAY_FACTOR
        }

        val decayedCategories = profile.categories.mapValues { (_, weight) ->
            (weight * decayMultiplier).takeIf { it > MIN_THRESHOLD }
        }.filterValues { it != null }.mapValues { it.value!! }

        val decayedKeywords = profile.keywords.mapValues { (_, weight) ->
            (weight * decayMultiplier).takeIf { it > MIN_THRESHOLD }
        }.filterValues { it != null }.mapValues { it.value!! }

        val decayedCreators = profile.creators.mapValues { (_, affinity) ->
            val newWeight = affinity.weight * decayMultiplier
            if (newWeight > MIN_THRESHOLD) {
                affinity.copy(weight = newWeight)
            } else null
        }.filterValues { it != null }.mapValues { it.value!! }

        val updated = profile.copy(
            categories = decayedCategories,
            keywords = decayedKeywords,
            creators = decayedCreators,
            lastDecayRun = System.currentTimeMillis()
        )

        interestProfileStore.save(updated)

        val cutoff = System.currentTimeMillis() - (21 * 24 * 60 * 60 * 1000L)
        signalEventDao.deleteOlderThan(cutoff)
    }
}
