package com.tiktokviewer.presentation.player

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClearDisplayController {

    private val _isClearMode = MutableStateFlow(false)
    val isClearMode: StateFlow<Boolean> = _isClearMode

    private val _autoRestoreTimeMs = MutableStateFlow(10000L)

    fun toggle() {
        _isClearMode.value = !_isClearMode.value
    }

    fun enable() {
        _isClearMode.value = true
    }

    fun disable() {
        _isClearMode.value = false
    }

    fun isInClearMode(): Boolean = _isClearMode.value
}
