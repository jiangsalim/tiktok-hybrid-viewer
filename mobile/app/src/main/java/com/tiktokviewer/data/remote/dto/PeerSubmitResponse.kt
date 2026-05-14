package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PeerSubmitResponse(
    @SerializedName("status") val status: String
)
