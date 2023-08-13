package com.akmal.androidtasklessons.ui.lessons

import androidx.lifecycle.viewModelScope
import com.akmal.androidtasklessons.data.repository.LessonRepository
import com.akmal.androidtasklessons.ui.base.BaseViewModel
import com.akmal.androidtasklessons.ui.lessons.model.LessonViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(private val repo: LessonRepository) : BaseViewModel() {

    init {
        getData()
    }

    private var _offers = MutableStateFlow<List<LessonViewData>>(emptyList())
    val offers: StateFlow<List<LessonViewData>> = _offers.asStateFlow()

    private fun getData() = viewModelScope.launch {
        repo.getLessons().onResult(
            success = { _offers.emit(it) },
            error = { _messageFlow.tryEmit(it.firstOrNull() ?: "") },
            loading = { _progressFlow.tryEmit(it) }
        )
    }
}