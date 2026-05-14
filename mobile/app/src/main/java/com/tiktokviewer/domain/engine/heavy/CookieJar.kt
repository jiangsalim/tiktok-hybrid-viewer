package com.tiktokviewer.domain.engine.heavy

import android.content.Context
import com.tiktokviewer.data.local.prefs.PreferencesManager

class CookieJar(context: Context) {

    private val prefs = PreferencesManager(context)

    fun saveCookies(cookies: String) {
        prefs.putString(KEY_COOKIES, cookies)
        prefs.putLong(KEY_COOKIES_UPDATED, System.currentTimeMillis())
    }

    fun loadCookies(): String? {
        val cookies = prefs.getString(KEY_COOKIES)
        if (cookies.isEmpty()) return null

        val age = System.currentTimeMillis() - prefs.getLong(KEY_COOKIES_UPDATED)
        if (age > MAX_COOKIE_AGE_MS) {
            clearCookies()
            return null
        }
        return cookies
    }

    fun clearCookies() {
        prefs.remove(KEY_COOKIES)
        prefs.remove(KEY_COOKIES_UPDATED)
    }

    fun hasValidCookies(): Boolean {
        return loadCookies() != null
    }

    companion object {
        private const val KEY_COOKIES = "tiktok_cookies"
        private const val KEY_COOKIES_UPDATED = "tiktok_cookies_updated"
        private const val MAX_COOKIE_AGE_MS = 24 * 60 * 60 * 1000L
    }
}
