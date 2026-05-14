package com.tiktokviewer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignatureCheckResponse(
    @SerializedName("status") val status: String,
    @SerializedName("latest_signature_version") val latestSignatureVersion: String?,
    @SerializedName("update_available") val updateAvailable: Boolean?,
    @SerializedName("update") val update: SignatureUpdateData? = null
)

data class SignatureUpdateData(
    @SerializedName("file_url") val fileUrl: String,
    @SerializedName("checksum_sha256") val checksumSha256: String,
    @SerializedName("file_size_bytes") val fileSizeBytes: Long?,
    @SerializedName("urgency") val urgency: String,
    @SerializedName("release_notes") val releaseNotes: String?
)
