package com.akmal.androidtasklessons.utils

sealed class ResultData<out T> {
    class Success<out T>(val data: T) : ResultData<T>()
    class Error(val message: List<String>) : ResultData<Nothing>()

    fun <R> map(transform: (T) -> R): ResultData<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }

    fun <R> flatMap(transform: (T) -> ResultData<R>): ResultData<R> = when (this) {
        is Success -> transform(data)
        is Error -> this
    }

    fun onSuccess(action: (T) -> Unit): ResultData<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    fun onError(action: (List<String>?) -> Unit): ResultData<T> {
        if (this is Error) {
            action(message)
        }
        return this
    }
}