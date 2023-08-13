package com.akmal.androidtasklessons.ui.video

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoViewModel : ViewModel() {

    private val _isFullscreen = MutableStateFlow(false)
    val isFullscreen: StateFlow<Boolean> = _isFullscreen.asStateFlow()

    fun toggleFullscreen() {
        _isFullscreen.value = !_isFullscreen.value
    }
}