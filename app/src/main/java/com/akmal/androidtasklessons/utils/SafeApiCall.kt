package com.akmal.androidtasklessons.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

interface SafeApiCall {
    suspend fun <T, K> safeApiCall(
        request: suspend () -> Response<T>,
        onSuccess: suspend (T?) -> K,
        cacheData: K? = null,
    ): Flow<ResultData<K>> = flow {
        if (cacheData != null) {
            emit(ResultData.Success(cacheData))
            return@flow
        }

        val response = request.invoke()
        if (response.isSuccessful && response.body() != null) {
            val baseResponse = response.body()
            val data = onSuccess.invoke(baseResponse)
            emit(ResultData.Success(data))
        } else {
            val error = response.errorBody()?.getErrorMessages() ?: listOf()
            emit(ResultData.Error(message = error))


        }
    }
        .catch {
            emit(ResultData.Error(listOf(it.message ?: "")))
        }
}

fun ResponseBody.getErrorMessages(): List<String> {
    return try {
        val list = mutableListOf<String>()
        val json = JSONObject(string())
        val message = json.optString("message")
        list.add(message)
        list
    } catch (e: Exception) {
        listOf()
    }
}
