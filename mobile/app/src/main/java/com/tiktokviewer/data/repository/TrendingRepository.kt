package com.tiktokviewer.data.repository

import com.tiktokviewer.domain.model.SearchResult

interface TrendingRepository {
    suspend fun getGlobalTrending(): SearchResult?
    suspend fun getCountryTrending(countryCode: String): SearchResult?
    suspend fun getCategoryTrending(categoryName: String): SearchResult?
}
