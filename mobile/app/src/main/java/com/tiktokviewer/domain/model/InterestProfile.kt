package com.tiktokviewer.domain.model

data class InterestProfile(
    val deviceId: String,
    val lastUpdated: Long,
    val categories: Map<String, Float>,
    val keywords: Map<String, Float>,
    val creators: Map<String, CreatorAffinity>,
    val hashtags: Map<String, Float>,
    val music: Map<String, Float>,
    val timePatterns: TimePatterns,
    val averageSessionMinutes: Float,
    val videosPerSession: Float,
    val completionRate: Float,
    val skipRate: Float,
    val lastDecayRun: Long
)
