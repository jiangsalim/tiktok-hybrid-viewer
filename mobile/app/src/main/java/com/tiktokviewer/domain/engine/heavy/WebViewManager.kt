package com.tiktokviewer.domain.engine.heavy

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class WebViewManager(
    private val resourceInterceptor: ResourceInterceptor,
    private val captchaDetector: CaptchaDetector
) {
    private var webView: WebView? = null

    fun setupWebView(view: WebView, userAgent: String, cookies: String?) {
        this.webView = view
        view.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            userAgentString = userAgent
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            allowFileAccess = false
            allowContentAccess = false
        }

        view.setWebContentsDebuggingEnabled(false)

        val cookieManager = android.webkit.CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(view, true)

        cookies?.let {
            cookieManager.removeAllCookies { }
            val cookieList = it.split(";")
            for (cookie in cookieList) {
                cookieManager.setCookie(".tiktok.com", cookie.trim())
            }
        }

        resourceInterceptor.reset()
    }

    fun getWebView(): WebView? = webView

    suspend fun loadAndWaitForResults(
        url: String,
        timeoutMs: Long = 12000
    ): HeavyEngineResult {
        val view = webView ?: return HeavyEngineResult.Failure("webview_not_initialized")
        val resultChannel = Channel<HeavyEngineResult>(Channel.BUFFERED)

        var hasLoaded = false
        val startTime = System.currentTimeMillis()

        view.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                resourceInterceptor.reset()
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                request?.url?.toString()?.let { reqUrl ->
                    resourceInterceptor.intercept(reqUrl)
                }
                return null
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (hasLoaded) return

                val pageUrl = url ?: ""
                val pageTitle = view?.title ?: ""

                val captchaResult = captchaDetector.detect("", pageTitle)
                if (captchaResult is CaptchaResult.CaptchaDetected) {
                    hasLoaded = true
                    resultChannel.trySend(HeavyEngineResult.Failure("captcha_detected"))
                    return
                }
                if (captchaResult is CaptchaResult.Blocked) {
                    hasLoaded = true
                    resultChannel.trySend(HeavyEngineResult.Failure("blocked_by_tiktok"))
                    return
                }

                if (resourceInterceptor.hasCaptured()) {
                    hasLoaded = true
                    resultChannel.trySend(
                        HeavyEngineResult.Success(
                            responseJson = resourceInterceptor.getCapturedResponse(),
                            source = "api_intercept"
                        )
                    )
                    return
                }

                view?.evaluateJavascript(
                    "document.querySelector('[data-e2e=\"search-video-item\"]') !== null"
                ) { hasVideos ->
                    if (hasVideos == "true" && !hasLoaded) {
                        hasLoaded = true
                        resultChannel.trySend(
                            HeavyEngineResult.Success(
                                responseJson = null,
                                source = "dom_ready"
                            )
                        )
                    }
                }
            }
        }

        view.loadUrl(url)

        return try {
            withTimeout(timeoutMs) {
                resultChannel.receive()
            }
        } catch (e: TimeoutCancellationException) {
            if (hasLoaded) {
                HeavyEngineResult.Failure("timeout_no_results")
            } else {
                HeavyEngineResult.Failure("timeout_page_not_loaded")
            }
        } catch (e: Exception) {
            HeavyEngineResult.Failure("heavy_engine_exception: ${e.message}")
        }
    }

    fun destroy() {
        webView?.destroy()
        webView = null
    }
}

sealed class HeavyEngineResult {
    data class Success(
        val responseJson: String?,
        val source: String
    ) : HeavyEngineResult()

    data class Failure(val errorCode: String) : HeavyEngineResult()
}
