package com.tiktokviewer.domain.model

data class SearchRequest(
    val id: String,
    val keyword: String,
    val cursor: String? = null,
    val count: Int = 20,
    val timestamp: Long = System.currentTimeMillis()
)
