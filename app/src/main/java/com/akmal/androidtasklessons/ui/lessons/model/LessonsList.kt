package com.akmal.androidtasklessons.ui.lessons.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LessonsList(
    val list: List<LessonViewData>
) : Parcelable