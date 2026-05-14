package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tiktokviewer.data.local.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDao {

    @Insert
    suspend fun insert(entry: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 50): List<SearchHistoryEntity>

    @Query("SELECT * FROM search_history WHERE keyword = :keyword ORDER BY searchedAt DESC LIMIT 1")
    suspend fun getByKeyword(keyword: String): SearchHistoryEntity?

    @Query("DELETE FROM search_history WHERE searchedAt < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()
}
