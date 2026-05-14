package com.tiktokviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tiktokviewer.data.local.entity.AppStateEntity

@Dao
interface AppStateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(state: AppStateEntity)

    @Query("SELECT * FROM app_state WHERE stateKey = :key LIMIT 1")
    suspend fun get(key: String): AppStateEntity?

    @Query("DELETE FROM app_state WHERE stateKey = :key")
    suspend fun delete(key: String)

    @Query("DELETE FROM app_state")
    suspend fun deleteAll()
}
