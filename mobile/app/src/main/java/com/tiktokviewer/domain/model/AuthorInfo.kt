package com.tiktokviewer.domain.model

data class AuthorInfo(
    val username: String,
    val displayName: String,
    val avatarUrl: String,
    val verified: Boolean,
    val followerCount: Long
)
