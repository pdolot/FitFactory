package com.example.fitfactory.data.models.response

import com.example.fitfactory.data.models.app.FitnessLesson
import com.google.gson.annotations.SerializedName

data class FitnessLessonResponse(
    @SerializedName("data")
    val data: List<FitnessLesson>? = null
) : BaseResponse()