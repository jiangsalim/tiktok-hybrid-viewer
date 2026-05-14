package com.tiktokviewer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tiktokviewer.data.local.dao.AppStateDao
import com.tiktokviewer.data.local.dao.FailureLogDao
import com.tiktokviewer.data.local.dao.PendingOperationDao
import com.tiktokviewer.data.local.dao.SearchCacheDao
import com.tiktokviewer.data.local.dao.SearchHistoryDao
import com.tiktokviewer.data.local.dao.SignalEventDao
import com.tiktokviewer.data.local.dao.VideoBookmarkDao
import com.tiktokviewer.data.local.dao.WatchHistoryDao
import com.tiktokviewer.data.local.entity.AppStateEntity
import com.tiktokviewer.data.local.entity.FailureLogEntity
import com.tiktokviewer.data.local.entity.PendingOperationEntity
import com.tiktokviewer.data.local.entity.SearchCacheEntity
import com.tiktokviewer.data.local.entity.SearchHistoryEntity
import com.tiktokviewer.data.local.entity.SignalEventEntity
import com.tiktokviewer.data.local.entity.VideoBookmarkEntity
import com.tiktokviewer.data.local.entity.WatchHistoryEntity

@Database(
    entities = [
        SignalEventEntity::class,
        SearchCacheEntity::class,
        VideoBookmarkEntity::class,
        WatchHistoryEntity::class,
        SearchHistoryEntity::class,
        FailureLogEntity::class,
        AppStateEntity::class,
        PendingOperationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun signalEventDao(): SignalEventDao
    abstract fun searchCacheDao(): SearchCacheDao
    abstract fun videoBookmarkDao(): VideoBookmarkDao
    abstract fun watchHistoryDao(): WatchHistoryDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun failureLogDao(): FailureLogDao
    abstract fun appStateDao(): AppStateDao
    abstract fun pendingOperationDao(): PendingOperationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tiktok_viewer_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
