package com.akmal.androidtasklessons.data.repository.impl

import com.akmal.androidtasklessons.data.mapper.toLessonViews
import com.akmal.androidtasklessons.data.remote.apiService.ApiService
import com.akmal.androidtasklessons.data.repository.LessonRepository
import com.akmal.androidtasklessons.ui.lessons.model.LessonViewData
import com.akmal.androidtasklessons.utils.ResultData
import com.akmal.androidtasklessons.utils.SafeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LessonRepositoryImpl @Inject constructor(private val apiService: ApiService) : LessonRepository, SafeApiCall {

    override suspend fun getLessons(): Flow<ResultData<List<LessonViewData>>> =
        safeApiCall(request = { apiService.getOffers() }, onSuccess = { it.toLessonViews() })
}