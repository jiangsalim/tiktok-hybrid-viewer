package com.tiktokviewer.domain.engine.light

import kotlinx.coroutines.delay
import kotlin.random.Random

class BehaviorSimulator {

    suspend fun preSearchDelay(minMs: Int = 1500, maxMs: Int = 5000) {
        val delayMs = Random.nextLong(minMs.toLong(), maxMs.toLong())
        delay(delayMs)

        if (Random.nextInt(100) < 30) {
            delay(Random.nextLong(200, 800))
        }
    }

    suspend fun simulateTyping(keyword: String, typoChance: Float = 0.05f) {
        for (char in keyword) {
            delay(Random.nextLong(50, 200))

            if (Random.nextFloat() < typoChance) {
                val typoChar = ('a' + Random.nextInt(26)).toString()
                delay(Random.nextLong(100, 300))
                delay(Random.nextLong(50, 150))
            }
        }
        delay(Random.nextLong(100, 300))
    }

    suspend fun randomDelay(minMs: Long = 500, maxMs: Long = 3000) {
        delay(Random.nextLong(minMs, maxMs))
    }
}
