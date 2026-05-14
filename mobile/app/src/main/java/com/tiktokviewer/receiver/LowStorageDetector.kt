package com.tiktokviewer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tiktokviewer.domain.engine.StorageManager

class LowStorageDetector : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_DEVICE_STORAGE_LOW) {
            handleLowStorage(context)
        }
    }

    private fun handleLowStorage(context: Context) {
        kotlinx.coroutines.MainScope().launch {
            try {
                val storageManager = StorageManager(
                    context,
                    com.tiktokviewer.domain.engine.cache.ThumbnailCacheManager(context),
                    com.tiktokviewer.domain.engine.cache.VideoCacheManager(context),
                    com.tiktokviewer.data.local.AppDatabase.getInstance(context).searchCacheDao()
                )
                storageManager.clearAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

private fun kotlinx.coroutines.MainScope.launch(block: suspend () -> Unit) {
    kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.Main) {
        block()
    }
}
