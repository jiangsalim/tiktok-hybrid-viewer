package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignatureProxySignResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("signed_url") val signedUrl: String?,
    @SerializedName("signed_headers") val signedHeaders: Map<String, String>?,
    @SerializedName("expires_at") val expiresAt: String?
)
