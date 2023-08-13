package com.akmal.androidtasklessons.ui.lessons.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LessonViewData(
    val description: String,
    val id: Int,
    val name: String,
    val thumbnail: String,
    val video_url: String
) : Parcelable
