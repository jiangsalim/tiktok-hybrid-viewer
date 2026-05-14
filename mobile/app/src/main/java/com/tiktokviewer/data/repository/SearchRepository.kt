package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.SearchRequest
import com.tiktokviewer.domain.model.SearchResult

interface SearchRepository {
    suspend fun search(request: SearchRequest): SearchResult
    suspend fun getCached(keyword: String): SearchResult?
    suspend fun clearCache()
}
