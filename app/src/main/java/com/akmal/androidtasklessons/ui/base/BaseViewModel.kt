package com.akmal.androidtasklessons.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akmal.androidtasklessons.utils.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel : ViewModel() {

    protected val _progressFlow = MutableStateFlow<Boolean>(false)
    val progressFlow = _progressFlow.asStateFlow()

    protected val _messageFlow = MutableSharedFlow<String>()
    val messageFlow = _messageFlow.asSharedFlow()


    inline fun <T> Flow<ResultData<T>>.onResult(
        crossinline success: suspend (T) -> Unit,
        crossinline error: suspend (List<String>) -> Unit,
        crossinline loading: (Boolean) -> Unit
    ) {
        loading(true)
        onEach { result ->
            when (result) {
                is ResultData.Success -> {
                    success(result.data)
                }

                is ResultData.Error -> {
                    error(result.message)
                }
            }
            loading(false)
        }.launchIn(viewModelScope)
    }
}