package com.tiktokviewer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "app_state",
    indices = [
        Index(value = ["stateKey"], unique = true)
    ]
)
data class AppStateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stateKey: String,
    val stateValue: String,
    val updatedAt: Long
)
