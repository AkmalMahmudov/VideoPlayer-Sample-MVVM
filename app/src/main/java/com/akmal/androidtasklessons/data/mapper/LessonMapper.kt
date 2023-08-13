package com.akmal.androidtasklessons.data.mapper

import com.akmal.androidtasklessons.data.remote.model.response.Lesson
import com.akmal.androidtasklessons.data.remote.model.response.LessonsResponse
import com.akmal.androidtasklessons.ui.lessons.model.LessonViewData

fun Lesson.toLessonView(): LessonViewData {
    return LessonViewData(
        description = this.description ?: "",
        id = this.id ?: -1,
        name = this.name ?: "",
        thumbnail = this.thumbnail ?: "",
        video_url = this.video_url ?: ""
    )
}

fun LessonsResponse?.toLessonViews(): List<LessonViewData> = this?.lessons?.map { it.toLessonView() } ?: emptyList()
