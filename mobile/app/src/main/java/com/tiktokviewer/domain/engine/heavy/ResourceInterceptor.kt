package com.tiktokviewer.domain.engine.heavy

import android.webkit.WebResourceResponse
import java.io.ByteArrayInputStream

class ResourceInterceptor {

    private var capturedJsonResponse: String? = null
    private var isCaptured = false

    fun intercept(requestUrl: String): WebResourceResponse? {
        if (isCaptured) return null

        if (requestUrl.contains("/api/search") ||
            requestUrl.contains("/api/recommend") ||
            requestUrl.contains("/api/item_list")
        ) {
            return null
        }
        return null
    }

    fun captureApiResponse(url: String, responseBody: String) {
        if (!isCaptured &&
            (url.contains("/api/search") ||
                    url.contains("/api/recommend") ||
                    url.contains("/api/item_list"))
        ) {
            capturedJsonResponse = responseBody
            isCaptured = true
        }
    }

    fun getCapturedResponse(): String? = capturedJsonResponse

    fun reset() {
        capturedJsonResponse = null
        isCaptured = false
    }

    fun hasCaptured(): Boolean = isCaptured
}
