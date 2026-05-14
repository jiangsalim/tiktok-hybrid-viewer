package com.tiktokviewer.domain.engine.light

import com.tiktokviewer.data.local.prefs.PreferencesManager
import com.tiktokviewer.domain.model.DeviceContext
import kotlin.random.Random

class DeviceSpoofer(private val prefs: PreferencesManager) {

    companion object {
        private const val KEY_PROFILE_CREATED = "device_profile_created"
        private const val KEY_ANDROID_VERSION = "device_android_version"
        private const val KEY_DEVICE_MODEL = "device_device_model"
        private const val KEY_SCREEN_WIDTH = "device_screen_width"
        private const val KEY_SCREEN_HEIGHT = "device_screen_height"
        private const val KEY_LANGUAGE = "device_language"
        private const val KEY_TIKTOK_VERSION = "device_tiktok_version"

        private val ANDROID_VERSIONS = listOf("13", "14", "15")
        private val DEVICE_MODELS = listOf(
            "SM-G991B", "Pixel 7", "Pixel 8", "OnePlus 11",
            "Xiaomi 13", "SM-S908B", "CPH2417", "Nothing Phone 2"
        )
        private val SCREEN_RESOLUTIONS = listOf(
            Pair(1080, 2340), Pair(1080, 2400), Pair(1440, 3120), Pair(1080, 2280)
        )
        private val LANGUAGES = listOf("en-US", "en-GB", "en")
        private const val TIKTOK_VERSION = "36.5.4"
    }

    fun buildContext(deviceId: String): DeviceContext {
        createProfileIfNeeded()

        return DeviceContext(
            deviceId = deviceId,
            userAgent = buildUserAgent(),
            androidVersion = prefs.getString(KEY_ANDROID_VERSION, "14"),
            deviceModel = prefs.getString(KEY_DEVICE_MODEL, "Pixel 7"),
            screenWidth = prefs.getInt(KEY_SCREEN_WIDTH, 1080),
            screenHeight = prefs.getInt(KEY_SCREEN_HEIGHT, 2340),
            language = prefs.getString(KEY_LANGUAGE, "en-US"),
            timezone = java.util.TimeZone.getDefault().id,
            tiktokVersion = prefs.getString(KEY_TIKTOK_VERSION, TIKTOK_VERSION)
        )
    }

    fun buildWebContext(): DeviceContext {
        val mobile = buildContext("")
        return mobile.copy(
            userAgent = buildWebUserAgent()
        )
    }

    fun getMobileUserAgent(): String = buildUserAgent()
    fun getWebUserAgent(): String = buildWebUserAgent()

    private fun buildUserAgent(): String {
        val androidVersion = prefs.getString(KEY_ANDROID_VERSION, "14")
        val deviceModel = prefs.getString(KEY_DEVICE_MODEL, "Pixel 7")
        val tiktokVersion = prefs.getString(KEY_TIKTOK_VERSION, TIKTOK_VERSION)

        return "com.zhiliaoapp.musically/$tiktokVersion (Linux; U; Android $androidVersion; " +
                "en-US; $deviceModel Build/SKQ1.210908.001; Cronet/TTNetVersion:ea3b7a44 " +
                "2024-08-22 QuicVersion:47e4f346 2024-06-19)"
    }

    private fun buildWebUserAgent(): String {
        val androidVersion = prefs.getString(KEY_ANDROID_VERSION, "14")
        val deviceModel = prefs.getString(KEY_DEVICE_MODEL, "Pixel 7")

        return "Mozilla/5.0 (Linux; Android $androidVersion; $deviceModel) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.6422.165 " +
                "Mobile Safari/537.36"
    }

    private fun createProfileIfNeeded() {
        if (prefs.getBoolean(KEY_PROFILE_CREATED)) return

        prefs.putString(KEY_ANDROID_VERSION, ANDROID_VERSIONS.random())
        prefs.putString(KEY_DEVICE_MODEL, DEVICE_MODELS.random())
        val res = SCREEN_RESOLUTIONS.random()
        prefs.putInt(KEY_SCREEN_WIDTH, res.first)
        prefs.putInt(KEY_SCREEN_HEIGHT, res.second)
        prefs.putString(KEY_LANGUAGE, LANGUAGES.random())
        prefs.putString(KEY_TIKTOK_VERSION, TIKTOK_VERSION)
        prefs.putBoolean(KEY_PROFILE_CREATED, true)
    }
}
