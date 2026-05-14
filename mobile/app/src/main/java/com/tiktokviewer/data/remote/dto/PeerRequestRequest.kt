package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerRequestRequest(
    @SerializedName("requester_id") val requesterId: String,
    @SerializedName("encrypted_query") val encryptedQuery: String,
    @SerializedName("timeout_ms") val timeoutMs: Int
)
