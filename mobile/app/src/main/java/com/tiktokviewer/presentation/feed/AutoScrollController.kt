package com.tiktokviewer.presentation.feed

import com.tiktokviewer.data.local.prefs.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AutoScrollController(private val prefs: PreferencesManager) {

    private val _autoScrollEnabled = MutableStateFlow(
        prefs.getBoolean(KEY_AUTO_SCROLL, true)
    )
    val autoScrollEnabled: StateFlow<Boolean> = _autoScrollEnabled

    fun toggle() {
        val newValue = !_autoScrollEnabled.value
        _autoScrollEnabled.value = newValue
        prefs.putBoolean(KEY_AUTO_SCROLL, newValue)
    }

    fun setEnabled(enabled: Boolean) {
        _autoScrollEnabled.value = enabled
        prefs.putBoolean(KEY_AUTO_SCROLL, enabled)
    }

    fun shouldAutoAdvance(currentPosition: Long, duration: Long): Boolean {
        if (!_autoScrollEnabled.value) return false
        if (duration <= 0) return false
        val progress = currentPosition.toFloat() / duration.toFloat()
        return progress >= 0.98f
    }

    companion object {
        private const val KEY_AUTO_SCROLL = "auto_scroll_enabled"
    }
}
