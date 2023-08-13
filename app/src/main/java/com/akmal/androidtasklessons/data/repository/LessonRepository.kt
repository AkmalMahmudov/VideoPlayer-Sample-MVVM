package com.akmal.androidtasklessons.data.repository

import com.akmal.androidtasklessons.ui.lessons.model.LessonViewData
import com.akmal.androidtasklessons.utils.ResultData
import kotlinx.coroutines.flow.Flow

interface LessonRepository {

    suspend fun getLessons(): Flow<ResultData<List<LessonViewData>>>
}