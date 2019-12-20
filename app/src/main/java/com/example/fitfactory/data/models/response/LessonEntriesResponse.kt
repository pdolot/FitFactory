package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.LessonEntry
import com.google.gson.annotations.SerializedName

data class LessonEntriesResponse(
    @SerializedName("data")
    val data: List<LessonEntry>? = null
): BaseResponse()